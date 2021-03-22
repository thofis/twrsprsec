package com.example.thofis.twrsprsec.security;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name = "sec_role")
public class UserRole {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;

  @Enumerated(EnumType.STRING)
  private Role role;

  public static UserRole fromRole(Role role) {
    UserRole userRole = new UserRole();
    userRole.setRole(role);
    return userRole;
  }

}
