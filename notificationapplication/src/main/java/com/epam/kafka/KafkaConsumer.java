package com.epam.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.epam.dto.notification.NotificationDto;
import com.epam.service.NotificationService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class KafkaConsumer {
	
	@Autowired
	NotificationService notificationService;

	@KafkaListener(topics = "notification",groupId = "notificationgroup")
	public void consume(NotificationDto notification) {
		System.out.println("notification " + notification);
		notificationService.sendNotification(notification);
	}
}
