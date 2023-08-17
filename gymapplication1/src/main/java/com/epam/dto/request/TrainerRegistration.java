package com.epam.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
@Data
public class TrainerRegistration {
	@NotBlank(message = "firstName is required")
	private String firstName;
	@NotBlank(message = "firstName is required")
    private String lastName;
	@NotBlank(message = "trainingType is required")
	@Pattern(regexp = "^(?)(fitness|yoga|Zumba|stretching|resistance)$", message = "Specialization should be fitness or yoga or Zumba or stretching or resistance")
    private String trainingType;
	@NotBlank(message = "email is required")
	@Email
	private String email; 
}
