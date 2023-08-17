package com.epam.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginCredentials {
	@NotBlank(message = "username is required")
	private String username;
	@NotBlank(message = "password is required")
    private String password;
}
