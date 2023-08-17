package com.epam.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.epam.dto.notification.NotificationDto;
import com.epam.dto.request.TrainingReportDto;



@Component
public class KafkaProducer {

	@Autowired
	KafkaTemplate<String, NotificationDto> kafkaNotificationTemplate;
	
	@Autowired
	KafkaTemplate<String, TrainingReportDto> kafkaReportTemplate;
	

	public void sendNotification(NotificationDto notificationDto) {
		kafkaNotificationTemplate.send("notification", notificationDto);
	}
	
	public void sendReport(TrainingReportDto reportDto)
	{
		kafkaReportTemplate.send("report", reportDto);
	}

}
