package co.com.jcgfdev.service;

import co.com.jcgfdev.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {

	Mono<User> createUser(User user);
	Flux<User> getAllUsers();
	
}
