package com.epam.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TraineesTrainers {
	@NotBlank(message = "lastName is required")
	private String traineeUsername;
	@Size(min = 1,message = "atleast 1 trainer is required")
	private List<String> trainersList;
}
