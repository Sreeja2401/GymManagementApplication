package com.epam.restcontroller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.dto.request.TrainingReportDto;
import com.epam.dto.response.ResponseReportDto;
import com.epam.service.TrainingReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TrainingsSummaryController.class)
public class TrainingsSummaryControllerTest {
	
	@MockBean
	TrainingReportService reportsService;
	@Autowired
	MockMvc mockMvc;
	
	TrainingReportDto trainingReportDto =new TrainingReportDto();
	
	@Test
	void saveTrainingReportTest() throws JsonProcessingException, Exception
	{
		Mockito.doNothing().when(reportsService).saveTrainingReport(any(TrainingReportDto.class));
		mockMvc.perform(post("/report/trainingReport")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(trainingReportDto))).andExpect(status().isAccepted()).andReturn();
	}
	@Test
	void getTrainingReportTest() throws JsonProcessingException, Exception
	{
		Mockito.when(reportsService.getTrainingReportByUsername(anyString())).thenReturn(new ResponseReportDto());
		mockMvc.perform(get("/report/trainingReport").param("userName", "testUsername")).andExpect(status().isOk()).andReturn();
	}
}
