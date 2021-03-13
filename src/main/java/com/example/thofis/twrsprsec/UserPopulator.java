package com.example.thofis.twrsprsec;

import com.example.thofis.twrsprsec.repository.UserRepository;
import com.example.thofis.twrsprsec.security.User;
import com.example.thofis.twrsprsec.security.UserPermission;
import com.example.thofis.twrsprsec.security.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

import static com.example.thofis.twrsprsec.security.Permission.PROCESS_HELLO;
import static com.example.thofis.twrsprsec.security.Permission.READ_ARTICLE;
import static com.example.thofis.twrsprsec.security.Role.ADMIN;
import static com.example.thofis.twrsprsec.security.Role.USER;
import static com.example.thofis.twrsprsec.security.UserPermission.fromPermission;
import static com.example.thofis.twrsprsec.security.UserRole.fromRole;

@Component
@RequiredArgsConstructor
public class UserPopulator implements ApplicationRunner {

  private final UserRepository userRepository;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    userRepository.save(newUser("Thomas", "Passw0rt", Set.of(fromRole(USER), fromRole(ADMIN)), Set.of(fromPermission(PROCESS_HELLO))));
    userRepository.save(newUser("John", "Passw0rt", Set.of(fromRole(USER)), Set.of(fromPermission(READ_ARTICLE))));
  }

  private User newUser(String username, String password, Set<UserRole> roles, Set<UserPermission> permissions) {
    return User.builder()
               .username(username)
               .password(password)
               .roles(roles)
               .permissions(permissions)
               .build();
  }

  private User newUser(String username, String password, Set<UserRole> roles) {
    return newUser(username, password, roles, Collections.emptySet());
  }
}
