package com.example.thofis.twrsprsec.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.EAGER;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "sec_user")
public class User implements UserDetails {
  @Id
  private String username;
  private String password;

  @OneToMany(cascade = ALL, fetch = EAGER)
  @JoinColumn(name = "user", nullable = false)
  private Set<PersistentRole> roles = new HashSet<>();

  @OneToMany(cascade = ALL, fetch = EAGER)
  @JoinColumn(name = "user", nullable = false)
  private Set<PersistentPermission> permissions = new HashSet<>();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<Permission> authorities = new HashSet<>();
    roles.forEach(persistentRole -> authorities.addAll(persistentRole.getRole()
                                                                     .getPermissions()));
    permissions.forEach(persistentPermission -> authorities.add(persistentPermission.getPermission()));
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
