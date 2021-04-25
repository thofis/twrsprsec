package com.example.thofis.twrsprsec.service;

import java.util.Optional;

import com.example.thofis.twrsprsec.repository.UserRepository;
import com.example.thofis.twrsprsec.security.AuthenticationFacade;
import com.example.thofis.twrsprsec.security.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleService {

  private final AuthenticationFacade authenticationFacade;

  private final UserRepository userRepository;


  public String readArticle(String id) {
    Authentication authentication = authenticationFacade.getAuthentication();
    log.info("retrieving Authentication with SecurityContextHolder. Type is {}", authentication.getClass()
                                                                                               .getSimpleName());
    log.info("authentication data: {}", authentication);
    Optional<User> authenticatedUser = userRepository.findById(authentication.getName());
    log.info("authenticated user: {}", authenticatedUser);
    return id;
  }

}
