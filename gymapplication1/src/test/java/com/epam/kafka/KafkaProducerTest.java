package com.epam.kafka;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import com.epam.dto.notification.NotificationDto;
import com.epam.dto.request.TrainingReportDto;

@ExtendWith(MockitoExtension.class)
public class KafkaProducerTest {

	@Mock
	private KafkaTemplate<String, NotificationDto> kafkaNotificationTemplate;

	@Mock
	private KafkaTemplate<String, TrainingReportDto> kafkaReportTemplate;

	@InjectMocks
	private KafkaProducer kafkaProducer;

	@Test
	void testSendNotification() {

		NotificationDto notificationDto = new NotificationDto();
		kafkaProducer.sendNotification(notificationDto);
		verify(kafkaNotificationTemplate).send(eq("notification"), any(NotificationDto.class));
	}

	@Test
	void testSendReport() {

		TrainingReportDto reportDto = new TrainingReportDto();
		kafkaProducer.sendReport(reportDto);
		verify(kafkaReportTemplate).send(eq("report"), any(TrainingReportDto.class));
	}
}
