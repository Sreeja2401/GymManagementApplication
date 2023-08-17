package com.epam.restcontroller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.epam.dto.request.TrainerProfileUpdate;
import com.epam.dto.request.TrainerRegistration;
import com.epam.dto.request.TrainerTrainingsRequestList;
import com.epam.dto.response.LoginCredentials;
import com.epam.dto.response.TrainerProfile;
import com.epam.dto.response.TrainingsDetailsResponse;
import com.epam.service.TrainerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TrainerRestController.class)
class TrainerRestControllerTest {

	@MockBean
	TrainerServiceImpl trainerService;

	@Autowired
	MockMvc mockMvc;

	TrainerRegistration trainerRegistration = new TrainerRegistration();
	TrainerProfileUpdate profileUpdate=new TrainerProfileUpdate();
	TrainerTrainingsRequestList trainingsRequestList = new TrainerTrainingsRequestList();
	

	@BeforeEach
	public void setup() {
		trainerRegistration.setFirstName("sreeja");
		trainerRegistration.setLastName("mangarapu");
		trainerRegistration.setEmail("sreejamangarapu@gmail.com");
		trainerRegistration.setTrainingType("fitness");

		trainingsRequestList.setUsername("sreeja");
		
		profileUpdate.setUsername("sreeja");
		profileUpdate.setFirstName("sreeja");
		profileUpdate.setLastName("mangarapu");
		profileUpdate.setEmail("sreejamangarapu@gmail.com");
		profileUpdate.setActive(true);
		profileUpdate.setSpectialization("fitness");
	

	}

	@Test
	void testAddTrainer() throws Exception {
		LoginCredentials credentials = new LoginCredentials();
		Mockito.when(trainerService.addTrainer(trainerRegistration)).thenReturn(credentials);

		mockMvc.perform(post("/gym/trainer/register").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(trainerRegistration))).andExpect(status().isCreated())
				.andReturn();
	}

	@Test
	void testGetTrainerProfile() throws Exception {
		TrainerProfile profile = new TrainerProfile();
		Mockito.when(trainerService.getTrainerProfile(anyString())).thenReturn(profile);

		mockMvc.perform(get("/gym/trainer").param("username", "testUsername")).andExpect(status().isOk()).andReturn();
	}

	@Test
	void testUpdateTrainerProfile() throws Exception {
		TrainerProfile trainerProfile=new TrainerProfile();
		Mockito.when(trainerService.updateTrainerProfile(profileUpdate)).thenReturn(trainerProfile);

		mockMvc.perform(put("/gym/trainer").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(profileUpdate))).andExpect(status().isCreated())
				.andReturn();
	}

	@Test
	void testGetTrainerTrainings() throws Exception {
		List<TrainingsDetailsResponse> trainingsList = new ArrayList<>(); 
		Mockito.when(trainerService.getTrainerTrainings(trainingsRequestList)).thenReturn(trainingsList);

		mockMvc.perform(post("/gym/trainer/trainings").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(trainingsRequestList))).andExpect(status().isOk())
				.andReturn();
	}
}
