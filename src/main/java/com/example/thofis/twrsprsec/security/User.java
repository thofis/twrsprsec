package com.example.thofis.twrsprsec.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@ToString
@Table(name = "sec_user")
public class User implements UserDetails {

	public static final String ROLE_PREFIX = "ROLE_";

	@Id
	private String username;

	private String password;

	@OneToMany(cascade = ALL, fetch = EAGER)
	@JoinColumn(name = "user", nullable = false)
	private Set<UserRole> roles = new HashSet<>();

	@OneToMany(cascade = ALL, fetch = EAGER)
	@JoinColumn(name = "user", nullable = false)
	private Set<UserPermission> permissions = new HashSet<>();

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		roles.forEach(userRole -> {
			// add the role itself as an authority
			authorities.add(userRole.getRole());
			// add all permissions included in the role
			authorities.addAll(userRole.getRole()
					.getPermissions());
		});
		// finally add individual additional permissions for the user
		permissions.forEach(userPermission -> authorities.add(userPermission.getPermission()));
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
