package com.example.thofis.twrsprsec.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private String jwtAudience;
  private String jwtIssuer;
  private String jwtSecret;
  private String jwtType;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                 String jwtAudience, String jwtIssuer,
                                 String jwtSecret, String jwtType) {
    this.jwtAudience = jwtAudience;
    this.jwtIssuer = jwtIssuer;
    this.jwtSecret = jwtSecret;
    this.jwtType = jwtType;
    this.setAuthenticationManager(authenticationManager);
    setFilterProcessesUrl("/login");
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    User user = (User) authResult.getPrincipal();
    SecretKey secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    // @formatter:off
    String token = Jwts.builder()
                       .signWith(secretKey, SignatureAlgorithm.HS512)
                       .setHeaderParam("typ", jwtType)
                       .setIssuer(jwtIssuer)
                       .setAudience(jwtAudience)
                       .setSubject(user.getUsername())
                       .setExpiration(Timestamp.valueOf(LocalDateTime.now()
                                                                     .plus(Duration.ofHours(2))))
                       .compact();
    // @formatter:on
    response.addHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", token));
  }
}
