package com.epam.entity;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@Document(collection = "report")
public class TrainingsSummary {
	@Id
	private String trainerUsername;
	private String  trainerFirstname;
	private String trainerLastname;
	private boolean trainerStatus ;
	private String email;
	private Map<Long ,Map<Long,Map<Long,Map<String,Long>>>> trainingSummary;
}