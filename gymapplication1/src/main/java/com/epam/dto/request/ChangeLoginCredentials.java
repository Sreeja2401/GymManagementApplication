package com.epam.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ChangeLoginCredentials {
	@NotBlank(message = "username is required")
	private String username;
	@NotBlank(message = "password is required")
    private String oldPassword;
	@NotBlank(message = "password is required")
    private String newPassword;
}
