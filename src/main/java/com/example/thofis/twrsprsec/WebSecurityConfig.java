package com.example.thofis.twrsprsec;

import com.example.thofis.twrsprsec.security.JwtAuthenticationFilter;
import com.example.thofis.twrsprsec.security.JwtAuthorizationFilter;
import com.example.thofis.twrsprsec.security.JwtConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import static com.example.thofis.twrsprsec.security.Permission.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtConfiguration jwtConfiguration;

  @Autowired
  private UserDetailsService userDetailsService;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
    http
        .addFilter(new JwtAuthenticationFilter(authenticationManager(),
                                               jwtConfiguration.getAudience(),
                                               jwtConfiguration.getIssuer(),
                                               jwtConfiguration.getSecret(),
                                               jwtConfiguration.getType()))
        .addFilter(new JwtAuthorizationFilter(authenticationManager(),
                                              jwtConfiguration.getAudience(),
                                              jwtConfiguration.getIssuer(),
                                              jwtConfiguration.getSecret(),
                                              jwtConfiguration.getType()))
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
//        .mvcMatchers(POST,"/api/login").permitAll()

        .mvcMatchers("/h2-console/**").permitAll()
        .mvcMatchers("/").permitAll()

        .anyRequest().denyAll();
//        .httpBasic();

        http.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());

        http
        .sessionManagement()
        .sessionCreationPolicy(STATELESS);

        http.cors().disable();
        http.csrf().disable();
        http.headers().frameOptions().disable();
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

  @Bean
  public HttpStatusEntryPoint unauthorizedEntryPoint() {

    return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
  }
}
