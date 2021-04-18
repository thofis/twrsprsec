package com.example.thofis.twrsprsec.controller;

import com.example.thofis.twrsprsec.service.ArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.Objects;

@RestController
@RequestMapping("/articles")
@Slf4j
@RequiredArgsConstructor
public class ArticleController {

  private final UserDetailsService userDetailsService;

  private final ArticleService articleService;

  @GetMapping("/{article_id}")
  public String getArticle(@PathVariable("article_id") String articleId, Principal principal) {
    Objects.requireNonNull(articleId);

    UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    log.info("authorities of user {} are : {}", userDetails.getUsername(), userDetails.getAuthorities()
                                                                                      .toString());

    String id = articleService.readArticle(articleId);

    log.info("loading article with id: {}", id);
    return id;
  }

  @PostMapping
  public ResponseEntity<Void> createArticle() {
    log.info("creating article");
    return ResponseEntity.created(UriComponentsBuilder.fromPath("/dummy")
                                                      .build()
                                                      .toUri())
                         .build();
  }

  @PutMapping("/{article_id}")
  public String updateArticle(@PathVariable("article_id") String articleId) {
    log.info("updating article with id: {}", articleId);
    return articleId;
  }

  @DeleteMapping("/{article_id}")
  public ResponseEntity<Void> deleteArticle(@PathVariable("article_id") String articleId) {
    log.info("deleting article with id: {}", articleId);
    return ResponseEntity.noContent()
                         .build();
  }


}
