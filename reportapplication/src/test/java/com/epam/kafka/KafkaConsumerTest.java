package com.epam.kafka;


import com.epam.dto.request.TrainingReportDto;
import com.epam.service.TrainingReportServiceimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ReportConsumerTest {

    @Mock
    private TrainingReportServiceimpl mockServiceImpl;

    @InjectMocks
    private KafkaConsumer reportConsumer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveTrainingReport() {
        TrainingReportDto reportDto = new TrainingReportDto();
        reportConsumer.consume(reportDto);
        Mockito.verify(mockServiceImpl, times(1)).saveTrainingReport(reportDto);
    }
}
