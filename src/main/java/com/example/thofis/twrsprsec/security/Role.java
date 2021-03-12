package com.example.thofis.twrsprsec.security;

import java.util.EnumSet;
import java.util.List;

import static com.example.thofis.twrsprsec.security.Permission.*;

public enum Role {
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

  private Role(Permission... permissions) {
    this.permissions.addAll(List.of(permissions));
  }


  private EnumSet<Permission> permissions = EnumSet.noneOf(Permission.class);

  public boolean hasPermission(Permission permission) {
    return permissions.contains(permission);
  }

  public EnumSet<Permission> getPermissions() {
    return permissions;
  }


}
