package com.example.thofis.twrsprsec.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@ToString
@Table(name = "sec_user")
public class User implements UserDetails {

	@Id
	private String username;

	private String password;

	@ElementCollection(targetClass = Role.class, fetch = EAGER)
	@Enumerated(STRING)
	@CollectionTable(name = "sec_user_roles")
	@Column(name = "role")
	private Set<Role> roles;

	@ElementCollection(targetClass = Permission.class, fetch = EAGER)
	@Enumerated(STRING)
	@CollectionTable(name = "sec_user_permissions")
	@Column(name = "permission")
	private Set<Permission> permissions;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Set<GrantedAuthority> authorities = new HashSet<>();
		roles.forEach(role -> {
			// add the role itself as an authority
			authorities.add(role);
			// add all permissions included in the role
			authorities.addAll(role.getPermissions());
		});
		// finally add individual additional permissions for the user
		permissions.forEach(authorities::add);
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
