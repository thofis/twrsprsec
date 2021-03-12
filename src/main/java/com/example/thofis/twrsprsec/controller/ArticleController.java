package com.example.thofis.twrsprsec.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@RestController
@RequestMapping("/articles")
@Slf4j
public class ArticleController {


  @GetMapping("/{article_id}")
  public String getArticle(@PathVariable("article_id") String articleId) {
    Objects.requireNonNull(articleId);
    log.info("loading article with id: {}", articleId);
    return articleId;
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
