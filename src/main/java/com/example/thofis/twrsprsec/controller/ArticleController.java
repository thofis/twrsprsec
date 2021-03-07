package com.example.thofis.twrsprsec.controller;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

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
		return ResponseEntity.created(UriComponentsBuilder.fromPath("/dummy").build().toUri()).build();
	}

	@PutMapping("/{article_id}")
	public String updateArticle(@PathVariable("article_id") String articleId) {
		log.info("updating article with id: {}", articleId);
		return articleId;
	}

	@DeleteMapping("/{article_id}")
	public ResponseEntity<Void> deleteArticle(@PathVariable("article_id") String articleId) {
		log.info("deleting article with id: {}", articleId);
		return ResponseEntity.noContent().build();
	}


}
