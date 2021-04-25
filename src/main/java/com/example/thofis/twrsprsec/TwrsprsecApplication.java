package com.example.thofis.twrsprsec;

import com.example.thofis.twrsprsec.security.JwtConfiguration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(JwtConfiguration.class)
public class TwrsprsecApplication {

  public static void main(String[] args) {
    SpringApplication.run(TwrsprsecApplication.class, args);
  }

}
