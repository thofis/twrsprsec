package com.example.thofis.twrsprsec.security;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {
  Authentication getAuthentication();
}
