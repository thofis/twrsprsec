package com.example.thofis.twrsprsec.repository;

import java.util.Optional;
import java.util.Set;

import com.example.thofis.twrsprsec.security.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.example.thofis.twrsprsec.security.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;

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
                    .roles(Set.of(USER))
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
