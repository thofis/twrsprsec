package com.example.thofis.twrsprsec;

import java.util.Set;

import com.example.thofis.twrsprsec.repository.UserRepository;
import com.example.thofis.twrsprsec.security.Permission;
import com.example.thofis.twrsprsec.security.Role;
import com.example.thofis.twrsprsec.security.User;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.example.thofis.twrsprsec.security.Permission.PROCESS_HELLO;
import static com.example.thofis.twrsprsec.security.Permission.READ_ARTICLE;
import static com.example.thofis.twrsprsec.security.Role.ADMIN;
import static com.example.thofis.twrsprsec.security.Role.USER;

@Component
@RequiredArgsConstructor
public class UserPopulator implements ApplicationRunner {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    userRepository.save(newUser("Thomas", passwordEncoder.encode("Passw0rt"), Set.of(USER, ADMIN) ,Set.of(PROCESS_HELLO)));
    userRepository.save(newUser("John", passwordEncoder.encode("Passw0rt"), Set.of(USER), Set.of(READ_ARTICLE)));
  }

  private User newUser(String username, String password, Set<Role> roles, Set<Permission> permissions) {
    return User.builder()
               .username(username)
               .password(password)
               .roles(roles)
               .permissions(permissions)
               .build();
  }
}
