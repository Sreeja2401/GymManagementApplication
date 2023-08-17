package com.epam.restcontroller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.dto.notification.NotificationDto;
import com.epam.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(NotificationRestController.class)
 class NotificationRestControllerTest {
	
	@MockBean
	NotificationService service;
	
	@Autowired
	MockMvc mockMvc;
	
	NotificationDto notificationDto=new NotificationDto();
	
	@Test
	void sendNotification() throws JsonProcessingException, Exception
	{
		Mockito.doNothing().when(service).sendNotification(any(NotificationDto.class));
		mockMvc.perform(post("/notification").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper()
				.writeValueAsString(notificationDto))).andExpect(status().isOk()).andReturn();
	}
	

}
