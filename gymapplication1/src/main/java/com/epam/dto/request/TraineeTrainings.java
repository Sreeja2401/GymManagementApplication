package com.epam.dto.request;

import jakarta.validation.constraints.NotBlank;

public class TraineeTrainings {
	
	 @NotBlank(message = "firstName is required")
	 private String traineeUsername;
	 
	 //• Period From (optional)
	// • Period To (optional)
	 private String trainerName;
	 private String trainingType;
	
}
