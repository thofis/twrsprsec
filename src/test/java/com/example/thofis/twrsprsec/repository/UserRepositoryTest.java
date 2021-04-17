package com.example.thofis.twrsprsec.repository;

import com.example.thofis.twrsprsec.security.Role;
import com.example.thofis.twrsprsec.security.User;
import com.example.thofis.twrsprsec.security.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

  public static final String TEST_USERNAME = "Tester";
  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  void setUp() {
    User user = User.builder()
                    .username(TEST_USERNAME)
                    .password("RandomPassword123")
                    .roles(Set.of(UserRole.fromRole(Role.USER)))
                    .build();
    userRepository.save(user);
  }

  @Test
  void findUserByUsername() {
    Optional<User> tester = userRepository.findById(TEST_USERNAME);
    assertThat(tester).isPresent();
    tester.ifPresent(user -> {
      assertThat(user.getUsername()).isEqualTo(TEST_USERNAME);
      assertThat(user.getPassword()).isNotBlank();
      assertThat(user.getRoles()).size()
                                 .isEqualTo(1);
    });
  }

  @AfterEach
  void tearDown() {
  }
}
