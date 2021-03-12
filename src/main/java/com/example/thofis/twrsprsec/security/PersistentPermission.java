package com.example.thofis.twrsprsec.security;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name = "sec_role")
public class PersistentPermission {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;

  @Enumerated(EnumType.STRING)
  private Permission permission;


  public static PersistentPermission fromPermission(Permission permission) {
    PersistentPermission persistentPermission = new PersistentPermission();
    persistentPermission.setPermission(permission);
    return persistentPermission;
  }
}
