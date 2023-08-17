package com.epam.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class TrainingDetails {
	@NotBlank(message = "traineeUsername is required")
	private String traineeUsername; 
	@NotBlank(message = "trainerUsername is required")
	private String trainerUsername;
	@NotBlank(message = "trainingName is required")
	private String trainingName; 
	@NotBlank(message = "trainingDate is required")
	private LocalDate trainingDate; 
	@NotBlank(message = "trainingType is required")
	private String trainingType;
	@NotBlank(message = "trainingDuration is required")
	private Long trainingDuration ;
}
