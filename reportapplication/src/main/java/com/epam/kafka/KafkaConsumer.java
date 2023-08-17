package com.epam.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.epam.dto.request.TrainingReportDto;
import com.epam.service.TrainingReportServiceimpl;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class KafkaConsumer {
	
	@Autowired
	TrainingReportServiceimpl reportService;

	@KafkaListener(topics = "report",groupId = "reportgroup")
	public void consume(TrainingReportDto report) {
		System.out.println("report " + report);
		reportService.saveTrainingReport(report);
	}
}
