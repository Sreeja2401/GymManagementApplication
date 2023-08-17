package com.epam.service;

import com.epam.dto.request.TrainingReportDto;
import com.epam.dto.response.ResponseReportDto;

public interface TrainingReportService {

	void saveTrainingReport(TrainingReportDto reportDto);

	ResponseReportDto getTrainingReportByUsername(String username) throws Exception;

}
