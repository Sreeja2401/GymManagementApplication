package com.epam.entity;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Data
@Document(value = "emailnotification")
public class EmailNotification {
	@Id
	private String id;
	private String fromEmail;
	private List<String> toEmails;
	private List<String> ccEmails;
	private String body;
	private String status;
	private String remarks;

}
