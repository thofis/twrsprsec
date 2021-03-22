package com.example.thofis.twrsprsec;

import com.example.thofis.twrsprsec.security.Permission;
import com.example.thofis.twrsprsec.service.HelloService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TwrsprsecApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	HelloService helloService;

	@Test
	void helloUnAuthenticated() {
		assertDoesNotThrow(() -> {
			mockMvc.perform(get("/hello"))
					.andExpect(status().isUnauthorized());
		});
	}

	@Test
		// sets up a security context with a mocked user
  @WithMockUser(username = "Thomas", password = "Passw0rt", authorities = {"PROCESS_HELLO"})
	void helloAuthenticated() {
		// how to mock authentication without @WithMockUser
//		UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken("Thomas", "Passw0rt");
//		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//		Mockito.when(securityContext.getAuthentication()).thenReturn(principal);
//		SecurityContextHolder.setContext(securityContext);

		assertDoesNotThrow(() -> {
			mockMvc.perform(get("/hello"))
					.andExpect(status().isOk());
		});
	}

	// sets up a security context with a mocked user
	@Test
	@WithMockUser(username = "Thomas", password = "Passw0rt", authorities = {"PROCESS_HELLO"})
	void helloAuthenticatedWithThomas() {
		assertDoesNotThrow(() -> {
			mockMvc.perform(get("/hello"))
					.andExpect(status().isOk());
		});
	}


	// sets up a security context with a mocked user
	@Test
	void helloAuthenticatedWithThomas2() {
		assertDoesNotThrow(() -> {
			mockMvc.perform(get("/hello")
					.with(SecurityMockMvcRequestPostProcessors.user("Thomas")
							.password("Passw0rt")
					.authorities(Permission.PROCESS_HELLO)))
					.andExpect(status().isOk());
		});
	}

	@Test
	void testPreAuthorizeNoUser() {
		assertThrows(AuthenticationCredentialsNotFoundException.class, () -> {
			helloService.processHello();
		});
	}

	@Test
	@WithMockUser(authorities = { "PROCESS_SOMETHING" })
	void testPreAuthorizeWrongAuthority() {
		assertThrows(AccessDeniedException.class, () -> {
			helloService.processHello();
		});
	}

	@Test
	@WithMockUser(authorities = { "PROCESS_HELLO" })
	void testPreAuthorizeOk() {
		assertDoesNotThrow(() -> {
			helloService.processHello();
		});
	}
}
