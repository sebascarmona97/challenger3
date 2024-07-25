package co.com.jcgfdev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class Challenge3Application {

	public static void main(String[] args) {
		SpringApplication.run(Challenge3Application.class, args);
	}

}
