package com.example.thofis.twrsprsec.security;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import static com.example.thofis.twrsprsec.security.Permission.CREATE_ARTICLE;
import static com.example.thofis.twrsprsec.security.Permission.CREATE_ORDER;
import static com.example.thofis.twrsprsec.security.Permission.DELETE_ARTICLE;
import static com.example.thofis.twrsprsec.security.Permission.DELETE_ORDER;
import static com.example.thofis.twrsprsec.security.Permission.READ_ARTICLE;
import static com.example.thofis.twrsprsec.security.Permission.READ_ORDER;
import static com.example.thofis.twrsprsec.security.Permission.WRITE_ARTICLE;
import static com.example.thofis.twrsprsec.security.Permission.WRITE_ORDER;

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

  // spring-security differentiates between role and other authorities with the prefix ROLE_
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
