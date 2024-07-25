package co.com.jcgfdev.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "Name cannot be null")
	@NotEmpty(message = "Name cannot be empty")
	private String name;
	
	@NotNull(message = "Email cannot be null")
	@Email(message = "Email should be valid")
	private String email;
	
	@NotNull(message = "Age cannot be null")
	@Min(value = 18, message = "Age should not be less than 18")
	@Max(value = 99, message = "Age should not be greater than 99")
	private Integer age;
	
}
