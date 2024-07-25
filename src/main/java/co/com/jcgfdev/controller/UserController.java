package co.com.jcgfdev.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import co.com.jcgfdev.exception.ErrorResponse;
import co.com.jcgfdev.exception.ResourceNotFoundException;
import co.com.jcgfdev.model.User;
import co.com.jcgfdev.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "User API", description = "User management APIs")
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

	@Autowired
	private UserService userService;

	@Operation(summary = "Create a new user")
	@PostMapping
	public Mono<ResponseEntity<Object>> createUser(@Valid @RequestBody User user) {
	    return userService.createUser(user)
	            .map(savedUser -> ResponseEntity.ok((Object) savedUser))
	            .onErrorResume(RuntimeException.class, e -> 
	                Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                        .body((Object) new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage())))
	            )
	            .onErrorResume(e -> 
	                Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
	            );
	}

	@Operation(summary = "Get all users")
	@GetMapping
    public Flux<User> getAllUsers() {
        return userService.getAllUsers()
                .onErrorResume(ResourceNotFoundException.class, e -> Flux.empty())
                .onErrorResume(e -> Flux.error(new RuntimeException("Internal Server Error")));
    }

	@ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationExceptions(WebExchangeBindException ex) {
        String message = ex.getBindingResult().getAllErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception ex) {
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    }

}
