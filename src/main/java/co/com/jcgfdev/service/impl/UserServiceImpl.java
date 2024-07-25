package co.com.jcgfdev.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.jcgfdev.exception.ResourceNotFoundException;
import co.com.jcgfdev.model.User;
import co.com.jcgfdev.repository.UserRepository;
import co.com.jcgfdev.service.UserService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public Mono<User> createUser(User user) {
		return Mono.fromCallable(() -> userRepository.save(user))
				.onErrorMap(e -> new RuntimeException("Error creating user: " + e.getMessage()));
	}

	@Override
	public Flux<User> getAllUsers() {
		return Mono.fromCallable(userRepository::findAll)
				.flatMapMany(users -> Flux.fromIterable(users)
						.switchIfEmpty(Mono.error(new ResourceNotFoundException("No users found"))));
	}

}
