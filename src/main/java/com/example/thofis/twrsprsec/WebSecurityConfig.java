package com.example.thofis.twrsprsec;

import java.util.List;

import javax.servlet.http.Cookie;

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
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;

import static com.example.thofis.twrsprsec.security.JwtAuthenticationFilter.JWT_COOKIE_NAME;
import static com.example.thofis.twrsprsec.security.Permission.CREATE_ARTICLE;
import static com.example.thofis.twrsprsec.security.Permission.CREATE_ORDER;
import static com.example.thofis.twrsprsec.security.Permission.DELETE_ARTICLE;
import static com.example.thofis.twrsprsec.security.Permission.DELETE_ORDER;
import static com.example.thofis.twrsprsec.security.Permission.READ_ARTICLE;
import static com.example.thofis.twrsprsec.security.Permission.READ_ORDER;
import static com.example.thofis.twrsprsec.security.Permission.WRITE_ARTICLE;
import static com.example.thofis.twrsprsec.security.Permission.WRITE_ORDER;
import static com.example.thofis.twrsprsec.security.Role.ADMIN;
import static com.example.thofis.twrsprsec.security.Role.USER;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtConfiguration jwtConfiguration;

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

				.mvcMatchers(GET, "/hello-admin").hasRole(ADMIN.name())
				.mvcMatchers(GET, "/hello-user").hasRole(USER.name())

				.mvcMatchers("/hello").authenticated()
//        .mvcMatchers(POST,"/api/login").permitAll()

				.mvcMatchers("/", "/h2-console/**", "/frontend/**", "jwt-from-cookie", "logout").permitAll()

				.anyRequest().denyAll();
//        .httpBasic();

		http
				.logout(logout -> logout
						.logoutUrl("/logout")
						.addLogoutHandler((request, response, auth) -> {
									for (Cookie cookie : request.getCookies()) {
										if (cookie.getName().equals(JWT_COOKIE_NAME)) {
											Cookie cookieToDelete = new Cookie(JWT_COOKIE_NAME, null);
											cookieToDelete.setMaxAge(0);
											response.addCookie(cookieToDelete);
										}
									}
								}
						));

		http.exceptionHandling().authenticationEntryPoint(unauthorizedEntryPoint());

		http
				.sessionManagement()
				.sessionCreationPolicy(STATELESS);

		http.cors().configurationSource(request -> {
			var cors = new CorsConfiguration();
			cors.setAllowedOrigins(List.of("http://localhost:9000"));
			cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
			cors.setAllowedHeaders(List.of("*"));
			return cors;
		});

//		http.csrf().disable();
		http.csrf()
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

		http.headers().frameOptions().disable();

		// @formatter:on
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.debug(true);
	}


	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public HttpStatusEntryPoint unauthorizedEntryPoint() {
		return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
	}
}
