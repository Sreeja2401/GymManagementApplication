package com.epam.dto.request;

import jakarta.validation.constraints.NotBlank;

public class TrainerTrainings {
	 @NotBlank(message = "trainerUsername is required")
	 private String trainerUsername;
	 //• Period From (optional)
	// • Period To (optional)
	 private String traineeUsername;
}
