package com.epam.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.dto.request.TrainingReportDto;
import com.epam.dto.response.ResponseReportDto;
import com.epam.entity.TrainingsSummary;
import com.epam.repository.TrainingsSummaryRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainingReportServiceimpl implements TrainingReportService {

	@Autowired
	TrainingsSummaryRepository reportRepository;

	@Override
	public void saveTrainingReport(TrainingReportDto reportDto) {
		log.info("entered into saveTrainingReport in TrainingReportServiceimpl");
		TrainingsSummary report = reportRepository.findById(reportDto.getTrainerUsername()).orElseGet(() -> {
			return TrainingsSummary.builder().trainerFirstname(reportDto.getTrainerFirstName())
					.trainerLastname(reportDto.getTrainerLastName()).trainerUsername(reportDto.getTrainerUsername())
					.trainerStatus(reportDto.isTrainerStatus()).email(reportDto.getEmail())

					.build();
		});
		
	    if(report.getTrainingSummary()==null)
	    {
		report.setTrainingSummary(new HashMap<>());
	    }
		long year = reportDto.getDate().getYear();
		long month = reportDto.getDate().getMonthValue();
		long day = reportDto.getDate().getDayOfMonth();
		Long duration = reportDto.getDuration();

		report.getTrainingSummary().computeIfAbsent(year, k -> new HashMap<>())
				.computeIfAbsent(month, k -> new HashMap<>())
				.computeIfAbsent(day, k->new HashMap<>())
				.put(reportDto.getDate().toString(), duration);
		
		reportRepository.save(report);
	}

	@Override
	public ResponseReportDto getTrainingReportByUsername(String username) throws Exception {
		TrainingsSummary report = reportRepository.findById(username)
				.orElseThrow(() -> new Exception("Trainer doesnt exist"));
		return ResponseReportDto.builder().trainerUsername(report.getTrainerLastname()).trainerFirstName(report.getTrainerFirstname())
				.trainerLastName(report.getTrainerLastname()).trainerStatus(report.isTrainerStatus()).trainingSummary(report.getTrainingSummary()).build();
	}

	

}