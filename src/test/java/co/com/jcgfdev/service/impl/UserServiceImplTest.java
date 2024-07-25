package co.com.jcgfdev.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import co.com.jcgfdev.exception.ResourceNotFoundException;
import co.com.jcgfdev.model.User;
import co.com.jcgfdev.repository.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class UserServiceImplTest {

	@InjectMocks
	private UserServiceImpl userService;

	@Mock
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
		user.setAge(26);

		when(userRepository.save(any(User.class))).thenReturn(user);

		Mono<User> result = userService.createUser(user);

		StepVerifier.create(result)
				.expectNextMatches(savedUser -> savedUser.getName().equals("Sebastian Carmona"))
				.verifyComplete();
	}

	@Test
	public void testGetAllUsers() {
		User user = new User();
		user.setName("Sebastian Carmona");
		user.setEmail("sebastiancrs97@gmail.com");
		user.setAge(26);

		List<User> users = Collections.singletonList(user);
		when(userRepository.findAll()).thenReturn(users);

		Flux<User> result = userService.getAllUsers();

		StepVerifier.create(result)
				.expectNextMatches(retrievedUser -> retrievedUser.getName().equals("Sebastian Carmona"))
				.verifyComplete();
	}
	
	@Test
    public void testGetAllUsersNoUsersFound() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        Flux<User> result = userService.getAllUsers();

        StepVerifier.create(result)
                .expectError(ResourceNotFoundException.class)
                .verify();
    }
}
