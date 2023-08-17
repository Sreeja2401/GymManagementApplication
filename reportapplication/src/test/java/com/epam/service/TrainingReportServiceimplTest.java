package com.epam.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.dto.request.TrainingReportDto;
import com.epam.dto.response.ResponseReportDto;
import com.epam.entity.TrainingsSummary;
import com.epam.repository.TrainingsSummaryRepository;

@ExtendWith(MockitoExtension.class)
 class TrainingReportServiceimplTest {
	
	@Mock
	TrainingsSummaryRepository reportRepository;
	
	@InjectMocks
	TrainingReportServiceimpl reportServiceimpl;
	
	TrainingsSummary report= TrainingsSummary.builder().build();
	TrainingReportDto reportDto= TrainingReportDto.builder()
			.trainerUsername("un")
			.trainerFirstName("fn")
			.trainerLastName("ln")
			.trainerStatus(true)
			.date(LocalDate.of(2022,04,02)).duration(3L).email("sreejamangarapu@gmail.com").build();
	
	
	@Test
	void testSaveTrainingReport()
	{
		Mockito.when(reportRepository.findById(anyString())).thenReturn(Optional.of(report));
		Mockito.when(reportRepository.save(any(TrainingsSummary.class))).thenReturn(report);
		reportServiceimpl.saveTrainingReport(reportDto);
		Mockito.verify(reportRepository,Mockito.times(1)).findById(anyString());
		Mockito.verify(reportRepository,Mockito.times(1)).save(any(TrainingsSummary.class));
		
	}

	@Test
	void getTrainingReportByUsernameTest() throws Exception
	{
		Mockito.when(reportRepository.findById(anyString())).thenReturn(Optional.of(report));
		ResponseReportDto response=reportServiceimpl.getTrainingReportByUsername("sreeja");
		Mockito.verify(reportRepository,Mockito.times(1)).findById(anyString());
		
	}

	@Test
	void getTrainingReportByUsernameTest_ExceptionCase()
	{
		Mockito.when(reportRepository.findById(anyString())).thenReturn(Optional.empty());
		assertThrows(Exception.class, ()->reportServiceimpl.getTrainingReportByUsername("sreeja"));
		Mockito.verify(reportRepository,Mockito.times(1)).findById(anyString());
		
	}

}
