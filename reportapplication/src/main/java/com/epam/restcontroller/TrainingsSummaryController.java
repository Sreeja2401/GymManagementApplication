package com.epam.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.dto.request.TrainingReportDto;
import com.epam.dto.response.ResponseReportDto;
import com.epam.service.TrainingReportService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/report/trainingReport")
public class TrainingsSummaryController {

	@Autowired
	TrainingReportService reportsService;

	@PostMapping
	public ResponseEntity<Void> saveTrainingReport(@Valid @RequestBody TrainingReportDto reportDto) {
		log.info("inside saveTrainingReport of TrainingsSummaryController with details:{}",reportDto);
		reportsService.saveTrainingReport(reportDto);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);

	}

	@GetMapping
	public ResponseEntity<ResponseReportDto> getTrainingReport(@RequestParam String userName) throws Exception {
		log.info("inside getTrainingReport of TrainingsSummaryController with details:{}",userName);
		return new ResponseEntity<>(reportsService.getTrainingReportByUsername(userName), HttpStatus.OK);

	}

}
