package com.epam.dto.response;

import java.util.Map;

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
public class ResponseReportDto {
	 private String trainerUsername;
     private String trainerFirstName;
     private String trainerLastName;
     private boolean trainerStatus;
     private String email;
     private Map<Long ,Map<Long,Map<Long,Long>>> trainingSummary;
}
