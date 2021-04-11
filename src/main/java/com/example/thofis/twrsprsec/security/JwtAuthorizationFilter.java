package com.example.thofis.twrsprsec.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.ObjectUtils;

import static com.example.thofis.twrsprsec.security.JwtAuthenticationFilter.JWT_COOKIE_NAME;
import static java.util.stream.Collectors.toSet;


@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

	private final String jwtSecret;


	public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
			String jwtAudience, String jwtIssuer, String jwtSecret, String jwtType) {
		super(authenticationManager);
		this.jwtSecret = jwtSecret;
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
	// @formatter:off
	return Arrays.
			stream(request.getCookies())
			.filter(cookie -> JWT_COOKIE_NAME.equals(cookie.getName()))
			.findAny()
			.map(cookie -> {
						String claims = cookie.getValue();
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
						}
						catch (JwtException e) {
							log.warn("Some exception: {} failed : {}", claims, e.getMessage());
							return null;
						}
					}
			).orElse(null);
		// @formatter:on
	}


	private Collection<SimpleGrantedAuthority> authoritiesFromString(String authoritiesString) {
		if (ObjectUtils.isEmpty(authoritiesString)) {
			return Collections.emptySet();
		}
		return Arrays.stream(authoritiesString.split("\\|"))
				.map(SimpleGrantedAuthority::new)
				.collect(toSet());
	}
}
