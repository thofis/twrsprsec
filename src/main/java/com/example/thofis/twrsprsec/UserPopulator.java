package com.example.thofis.twrsprsec;

import com.example.thofis.twrsprsec.repository.UserRepository;
import com.example.thofis.twrsprsec.security.PersistentPermission;
import com.example.thofis.twrsprsec.security.PersistentRole;
import com.example.thofis.twrsprsec.security.User;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;

import static com.example.thofis.twrsprsec.security.Permission.READ_ARTICLE;
import static com.example.thofis.twrsprsec.security.PersistentPermission.fromPermission;
import static com.example.thofis.twrsprsec.security.PersistentRole.fromRole;
import static com.example.thofis.twrsprsec.security.Role.ADMIN;
import static com.example.thofis.twrsprsec.security.Role.USER;

@Component
@RequiredArgsConstructor
public class UserPopulator implements ApplicationRunner {

  private final UserRepository userRepository;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    userRepository.save(newUser("Thomas", "Passw0rt", Set.of(fromRole(USER), fromRole(ADMIN))));
    userRepository.save(newUser("John", "Passw0rt", Set.of(fromRole(USER)), Set.of(fromPermission(READ_ARTICLE))));
  }

  private User newUser(String username, String password, Set<PersistentRole> roles, Set<PersistentPermission> permissions) {
    return User.builder()
               .username(username)
               .password(password)
               .roles(roles)
               .permissions(permissions)
               .build();
  }

  private User newUser(String username, String password, Set<PersistentRole> roles) {
    return newUser(username, password, roles, Collections.emptySet());
  }
}
