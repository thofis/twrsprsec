package com.example.thofis.twrsprsec.security;

import lombok.*;
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
@ToString
@Table(name = "sec_user")
public class User implements UserDetails {
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
    Set<Permission> authorities = new HashSet<>();
    roles.forEach(userRole -> authorities.addAll(userRole.getRole()
                                                         .getPermissions()));
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
