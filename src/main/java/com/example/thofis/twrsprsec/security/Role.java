package com.example.thofis.twrsprsec.security;

import org.springframework.security.core.GrantedAuthority;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static com.example.thofis.twrsprsec.security.Permission.*;

public enum Role implements GrantedAuthority {
  ADMIN(
      CREATE_ARTICLE,
      READ_ARTICLE,
      WRITE_ARTICLE,
      DELETE_ARTICLE,
      CREATE_ORDER,
      DELETE_ORDER,
      READ_ORDER,
      WRITE_ORDER
  ),
  USER(
      CREATE_ORDER,
      READ_ORDER,
      WRITE_ORDER,
      DELETE_ORDER
  );

  public static final String ROLE_PREFIX = "ROLE_";

  private Role(Permission... permissions) {
    this.permissions.addAll(List.of(permissions));
  }


  private EnumSet<Permission> permissions = EnumSet.noneOf(Permission.class);

  public Set<Permission> getPermissions() {
    return permissions;
  }

  @Override
  public String getAuthority() {
    return ROLE_PREFIX + this.name();
  }
}
