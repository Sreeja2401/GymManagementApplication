package com.epam.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TraineeRegistrationDetails {
	@NotBlank(message = "firstName is required")
	private String firstName;
	@NotBlank(message = "lastName is required")
    private String lastName;
	@Email(message = "email required")
	private String email; 
	private LocalDate dateOfBirth;
    private String address;
}
