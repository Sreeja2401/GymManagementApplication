package com.epam.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TraineeProfileUpdate {
	@NotBlank(message = "username is required")
	private String username;
	@NotBlank(message = "firstName is required")
	private String firstName;
	@NotBlank(message = "LastName is required")
	private String lastName;
	@Email
	private String email;
	@NotNull(message = "date cannot be null")
	private LocalDate dateOfBirth;
	@NotBlank(message = "address is required")
	private String address;
	@AssertTrue(message = "isActive must be true or false")
	private boolean isActive;
}
