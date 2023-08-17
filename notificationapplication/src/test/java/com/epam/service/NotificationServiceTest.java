package com.epam.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.epam.dto.notification.NotificationDto;
import com.epam.entity.EmailNotification;
import com.epam.repository.NotificationRepository;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

	@Mock
	JavaMailSender javaMailSender;
	@Mock
	NotificationRepository notificationRepository;

	@InjectMocks
	NotificationService notificationService;

	EmailNotification emailNotification;
	NotificationDto notificationDto;

	@BeforeEach
	void setUp() {
		EmailNotification.builder().build();
		notificationDto = new NotificationDto();
		notificationDto.setBody("hi");
		notificationDto.setCcEmails(List.of());
		notificationDto.setSubject("hello");
		notificationDto.setToEmails(List.of());
	}

	@Test
	void sendNotificationTest() {

		Mockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
		Mockito.when(notificationRepository.save(any(EmailNotification.class))).thenReturn(emailNotification);
		notificationService.sendNotification(notificationDto);
		Mockito.verify(javaMailSender,Mockito.times(1)).send(any(SimpleMailMessage.class));
		Mockito.verify(notificationRepository,Mockito.times(1)).save(any(EmailNotification.class));

	}
	@Test
	void testsendNotificationExceptionCase() {
		doThrow(new MailSendException("mail sending unsuccessfull")).when(javaMailSender).send(any(SimpleMailMessage.class));
		notificationService.sendNotification(notificationDto);
		Mockito.verify(javaMailSender,Mockito.times(1)).send(any(SimpleMailMessage.class));
        
	}

}
