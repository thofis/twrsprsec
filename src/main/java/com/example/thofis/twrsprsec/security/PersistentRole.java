package com.example.thofis.twrsprsec.security;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table(name = "sec_permission")
public class PersistentRole {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;

  @Enumerated(EnumType.STRING)
  private Role role;

  public static PersistentRole fromRole(Role role) {
    PersistentRole persistentRole = new PersistentRole();
    persistentRole.setRole(role);
    return persistentRole;
  }

}
