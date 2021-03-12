package com.example.thofis.twrsprsec.security;

import org.springframework.security.core.GrantedAuthority;

public enum Permission implements GrantedAuthority {
  READ_ORDER,
  WRITE_ORDER,
  CREATE_ORDER,
  DELETE_ORDER,
  READ_ARTICLE,
  WRITE_ARTICLE,
  CREATE_ARTICLE,
  DELETE_ARTICLE;

  @Override
  public String getAuthority() {
    return name();
  }


}
