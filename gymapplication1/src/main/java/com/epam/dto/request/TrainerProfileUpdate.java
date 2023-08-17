package com.epam.dto.request;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class TrainerProfileUpdate {
	 @NotBlank(message = "username is required")
	 private String username;
	 @NotBlank(message = "firstName is required")
	 private String firstName;
	 @NotBlank(message = "LastName is required")
	 private String lastName;
	 @Email
	 private String email; 
	 @NotBlank
	 private String spectialization;
	 @AssertTrue(message = "isActive must be true or false")
	 private boolean isActive;
}
