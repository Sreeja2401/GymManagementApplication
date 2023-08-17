package com.epam.dto.response;

import java.util.List;

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
public class TrainerProfile {
	private String trainerUsername;
	private String trainerFirstName;
	private String trainerLastName;
	private String trainerSpecialization;
	private boolean isActive;
	private List<TraineeDto> traineeList; 

}
