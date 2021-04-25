package com.example.thofis.twrsprsec.security;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfiguration {
  private String secret;
  private String issuer;
  private String audience;
  private String type;
}
