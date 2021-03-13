package com.example.thofis.twrsprsec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.thofis.twrsprsec.security.Permission.*;
import static org.springframework.http.HttpMethod.*;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
        .authorizeRequests()

        .mvcMatchers(GET, "/articles/**").hasAuthority(READ_ARTICLE.name())
        .mvcMatchers(POST, "/articles/**").hasAuthority(CREATE_ARTICLE.name())
        .mvcMatchers(PUT, "/articles/**").hasAuthority(WRITE_ARTICLE.name())
        .mvcMatchers(DELETE, "/articles/**").hasAuthority(DELETE_ARTICLE.name())

        .mvcMatchers(GET, "/orders/**").hasAuthority(READ_ORDER.name())
        .mvcMatchers(POST, "/orders/**").hasAuthority(CREATE_ORDER.name())
        .mvcMatchers(PUT, "/orders/**").hasAuthority(WRITE_ORDER.name())
        .mvcMatchers(DELETE, "/orders/**").hasAuthority(DELETE_ORDER.name())

        .mvcMatchers("/hello").authenticated()

        .mvcMatchers("/h2-console/**").permitAll()
        .mvcMatchers("/").permitAll()

        .anyRequest().denyAll()
        .and()
        .httpBasic();

      http.csrf()
          .disable();
      http.headers()
          .frameOptions()
          .disable();
    // @formatter:on
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.debug(true);
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return NoOpPasswordEncoder.getInstance();
  }

}
