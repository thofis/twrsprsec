package com.example.thofis.twrsprsec.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static java.util.stream.Collectors.toSet;


@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  private final String jwtSecret;
  private final String jwtIssuer;
  private final String jwtType;
  private final String jwtAudience;


  public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                String jwtAudience, String jwtIssuer, String jwtSecret, String jwtType) {
    super(authenticationManager);
    this.jwtAudience = jwtAudience;
    this.jwtIssuer = jwtIssuer;
    this.jwtSecret = jwtSecret;
    this.jwtType = jwtType;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    UsernamePasswordAuthenticationToken authenticationToken = parseToken(request);
    if (authenticationToken != null) {
      SecurityContextHolder.getContext()
                           .setAuthentication(authenticationToken);
    }
    chain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken parseToken(HttpServletRequest request) {
    String token = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (token != null && token.startsWith("Bearer ")) {
      log.info("processing bearer token: {}", token);
      String claims = token.replace("Bearer ", "");
      try {
        Jws<Claims> claimsJws = Jwts.parser()
                                    .setSigningKey(jwtSecret.getBytes())
                                    .parseClaimsJws(claims);
        String username = (String) claimsJws.getBody()
                                            .get("subject");
        if ("".equals(username) || username == null) {
          return null;
        }
        Collection<SimpleGrantedAuthority> authorities = authoritiesFromString((String) claimsJws.getBody()
                                                                                                 .get("authorities"));
        log.info("authorizing user: {} with authorities: {}", username, authorities);
        // authorities could alternatively retrieved from db (findByUsername)
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
      } catch (JwtException e) {
        log.warn("Some exception: {} failed : {}", token, e.getMessage());
      }
    }
    return null;
  }


  private Collection<SimpleGrantedAuthority> authoritiesFromString(String authorititiesString) {
    if (ObjectUtils.isEmpty(authorititiesString)) {
      return Collections.emptySet();
    }

    return Arrays.stream(authorititiesString.split("\\|"))
                 .map(SimpleGrantedAuthority::new)
                 .collect(toSet());
  }
}
