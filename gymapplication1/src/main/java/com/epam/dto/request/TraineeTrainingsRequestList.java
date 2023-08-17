package com.epam.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
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
public class TraineeTrainingsRequestList {
    @NotBlank(message = "User Name is required")
    private String username;
    private LocalDate periodFrom;
    private LocalDate periodTo;
    private String trainerName;
    private String trainingType;
    
}