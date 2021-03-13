package com.example.thofis.twrsprsec.security;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name = "sec_role")
public class UserPermission {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;

  @Enumerated(EnumType.STRING)
  private Permission permission;


  public static UserPermission fromPermission(Permission permission) {
    UserPermission userPermission = new UserPermission();
    userPermission.setPermission(permission);
    return userPermission;
  }
}
