package com.example.thofis.twrsprsec.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  private final String jwtSecret;
  private final String jwtIssuer;
  private final String jwtType;
  private final String jwtAudience;

  private final UserDetailsService userDetailsService;

  public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                UserDetailsService userDetailsService, String jwtAudience, String jwtIssuer, String jwtSecret, String jwtType) {
    super(authenticationManager);
    this.jwtAudience = jwtAudience;
    this.jwtIssuer = jwtIssuer;
    this.jwtSecret = jwtSecret;
    this.jwtType = jwtType;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    UsernamePasswordAuthenticationToken authenticationToken = parseToken(request);
    if (authenticationToken != null) {
      SecurityContextHolder.getContext()
                           .setAuthentication(authenticationToken);
    } else {
      SecurityContextHolder.clearContext();
    }
    chain.doFilter(request, response);
  }

  private UsernamePasswordAuthenticationToken parseToken(HttpServletRequest request) {
    String token = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (token != null && token.startsWith("Bearer ")) {
      String claims = token.replace("Bearer ", "");
      try {
        Jws<Claims> claimsJws = Jwts.parser()
                                    .setSigningKey(jwtSecret.getBytes())
                                    .parseClaimsJws(claims);
        String username = claimsJws.getBody()
                                   .getSubject();
        if ("".equals(username) || username == null) {
          return null;
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
      } catch (JwtException e) {
        log.warn("Some exception: {} failed : {}", token, e.getMessage());
      }
    }
    return null;
  }
}
