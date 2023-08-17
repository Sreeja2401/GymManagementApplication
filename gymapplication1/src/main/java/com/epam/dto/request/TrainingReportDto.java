package com.epam.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TrainingReportDto {
      private String trainerUsername;
      private String trainerFirstName;
      private String trainerLastName;
      private boolean trainerStatus;
      private String email;
      private LocalDate date;
      private Long duration;
           
}
