package com.epam.kafka;

import com.epam.dto.notification.NotificationDto;
import com.epam.service.NotificationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
 class NotificationConsumerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private KafkaConsumer notificationConsumer;

    @Test
    public void testSendNotificationLog() {
        NotificationDto notificationDto = new NotificationDto();
        notificationConsumer.consume(notificationDto);
        Mockito.verify(notificationService, times(1)).sendNotification(notificationDto);
    }
}