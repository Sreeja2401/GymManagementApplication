package com.epam.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class TrainingsDetailsResponse {
	private String trainingName;
	private LocalDate trainingDate;
	private String trainingType;
	private Long trainingDuration;
	private String trainerName;
	private String traineeName;
	
}
