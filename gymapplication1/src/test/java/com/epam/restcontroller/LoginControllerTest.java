package com.epam.restcontroller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.dto.response.LoginCredentials;
import com.epam.service.LoginService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(LoginRestController.class)
 class LoginControllerTest {

    @MockBean
    LoginService loginService;

    @Autowired
    MockMvc mockMvc;
    
    LoginCredentials credentials = new LoginCredentials();
    
    @BeforeEach
    void setup()
    {
    	credentials.setUsername("sreeja");
    	credentials.setPassword("rfyhacvxcasc");
    
    }

	/*
	 * @Test void testUserAuthentication() throws Exception {
	 * 
	 * mockMvc.perform(post("/gym/login/authentication")
	 * .contentType(MediaType.APPLICATION_JSON) .content(new
	 * ObjectMapper().writeValueAsString(credentials))) .andExpect(status().isOk());
	 * 
	 * Mockito.verify(loginService,
	 * Mockito.times(1)).userAuthentication(any(LoginCredentials.class)); }
	 */

    @Test
    void testUpdateCredentials() throws Exception {
    	Mockito.doNothing().when(loginService).updateCredentials(credentials, "new");
        String newPassword = "newPassword";
        mockMvc.perform(post("/gym/login/update")
                .param("newPassword", newPassword)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(credentials)))
                .andExpect(status().isOk());

        Mockito.verify(loginService, Mockito.times(1)).updateCredentials(any(LoginCredentials.class), eq(newPassword));
    }
    @Test
    void testMethodArgumentInvalidException() throws JsonProcessingException, Exception {
    	LoginCredentials credentials = new LoginCredentials();
    	String newPassword = "";
        mockMvc.perform(post("/gym/login/update")
        		 .param("newPassword", newPassword)
        		.contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(credentials))).andExpect(status().isBadRequest())
                .andReturn();

    }

    @Test
    void testHttpMessageNotReadableException() throws JsonProcessingException, Exception {
        Mockito.doNothing().when(loginService).updateCredentials(credentials, "new");
        mockMvc.perform(post("/gym/login/update").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString("{username:"))).andExpect(status().isBadRequest())
                .andReturn();
    }
    
    
    
}

