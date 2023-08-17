package com.epam.dto.notification;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
	private List<String> toEmails;
	private List<String> ccEmails;
	private String subject;
	private String body;

}
