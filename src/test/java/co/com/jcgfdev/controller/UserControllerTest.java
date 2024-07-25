package co.com.jcgfdev.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.com.jcgfdev.exception.ErrorResponse;
import co.com.jcgfdev.exception.ResourceNotFoundException;
import co.com.jcgfdev.model.User;
import co.com.jcgfdev.repository.UserRepository;
import co.com.jcgfdev.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(UserController.class)
public class UserControllerTest {
	
	@Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void testCreateUser() {
        User user = new User();
        user.setName("Sebastian Carmona");
        user.setEmail("sebastiancrs97@gmail.com");
        user.setAge(25);

        when(userService.createUser(any(User.class))).thenReturn(Mono.just(user));

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isOk()
                .expectBody(User.class)
                .isEqualTo(user);
    }
    
    @Test
    public void testGetAllUsers() {
        User user = new User();
        user.setName("Sebastian Carmona");
        user.setEmail("sebastiancrs97@gmail.com");
        user.setAge(25);

        List<User> users = Collections.singletonList(user);
        when(userService.getAllUsers()).thenReturn(Flux.fromIterable(users));

        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .hasSize(1)
                .contains(user);
    }
    
    @Test
    public void testGetAllUsersNoUsersFound() {
        when(userService.getAllUsers()).thenReturn(Flux.error(new ResourceNotFoundException("No users found")));

        webTestClient.get().uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .hasSize(0);
    }
    
    @Test
    public void testHandleValidationException() {
        User user = new User();
        user.setName("");  // Invalid name
        user.setEmail("sebastiancrs97@gmail.com");
        user.setAge(25);

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .consumeWith(response -> {
                    ErrorResponse error = response.getResponseBody();
                    assert error != null;
                    assert error.getMessage().contains("Name cannot be empty");
                });
    }
    
    @Test
    public void testHandleException() {
        when(userService.createUser(any(User.class))).thenReturn(Mono.error(new RuntimeException("Internal Server Error")));

        User user = new User();
        user.setName("Sebastian Carmona");
        user.setEmail("sebastiancrs97@gmail.com");
        user.setAge(25);

        webTestClient.post().uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(ErrorResponse.class)
                .consumeWith(response -> {
                    ErrorResponse error = response.getResponseBody();
                    assert error != null;
                    assert error.getMessage().contains("Internal Server Error");
                });
    }

}
