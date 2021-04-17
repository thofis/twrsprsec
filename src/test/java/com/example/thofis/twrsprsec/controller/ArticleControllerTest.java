package com.example.thofis.twrsprsec.controller;

import com.example.thofis.twrsprsec.security.User;
import com.example.thofis.twrsprsec.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ArticleController.class)
// disable spring security filters for this test
@AutoConfigureMockMvc(addFilters = false)
class ArticleControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ArticleService articleService;

  @MockBean
  private UserDetailsService userDetailsService;

  @Test
    // as a simple example for a test with disabled spring security filters
  void shouldDeleteArticle() throws Exception {
    mockMvc.perform(delete("/articles/{article_id}", "124"))
           .andExpect(status().isNoContent());
  }

  @Test
    // more complicated example, because the tested method uses Principal and UserDetailsService
    // -> those dependencies have to be mocked out during this tests.
  void shouldReturnCorrectArticle() throws Exception {
    String articleId = "123";
    String username = "Test";
    User testUser = User.builder()
                        .username(username)
                        .roles(Collections.emptySet())
                        .permissions(Collections.emptySet())
                        .build();
    when(articleService.readArticle(articleId)).thenReturn(articleId);
    when(userDetailsService.loadUserByUsername(username)).thenReturn(testUser);
    mockMvc.perform(get("/articles/{article_id}", "123").principal(new Principal() {
      @Override
      public String getName() {
        return username;
      }
    }))
           .andExpect(status().isOk())
           .andExpect(content().string(articleId));
  }


}
