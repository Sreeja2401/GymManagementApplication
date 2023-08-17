package com.epam.restcontroller;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

import com.epam.dto.request.TraineeProfileUpdate;
import com.epam.dto.request.TraineeRegistrationDetails;
import com.epam.dto.request.TraineeTrainingsRequestList;
import com.epam.dto.response.LoginCredentials;
import com.epam.dto.response.TraineeProfile;
import com.epam.dto.response.TrainerDto;
import com.epam.dto.response.TrainingsDetailsResponse;
import com.epam.service.TraineeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(TraineeRestController.class)
class TraineeRestControllerTest {

	@MockBean
	TraineeServiceImpl traineeService;

	@Autowired
	MockMvc mockMvc;

	TraineeRegistrationDetails registrationDetails = new TraineeRegistrationDetails();
	TraineeProfileUpdate profileUpdate=new TraineeProfileUpdate();
	TraineeTrainingsRequestList trainingsRequestList = new TraineeTrainingsRequestList();
	TraineeProfile traineeProfile=new TraineeProfile();

	@BeforeEach
	public void setup() {

		registrationDetails.setFirstName("sreeja");
		registrationDetails.setLastName("mangarapu");
		registrationDetails.setEmail("sreejamangarapu@gmail.com");
		registrationDetails.setAddress("nirmal");
		registrationDetails.setDateOfBirth(java.time.LocalDate.parse("2001-07-31"));

		trainingsRequestList.setUsername("sreeja");
		trainingsRequestList.setTrainingType("fitness");
		
		profileUpdate.setUsername("sreeja");
		profileUpdate.setFirstName("sree");
		profileUpdate.setLastName("mangarapu");
		profileUpdate.setEmail("sreeja@gmail.com");
		profileUpdate.setDateOfBirth(java.time.LocalDate.parse("2001-07-31"));
		profileUpdate.setAddress("nirmal");
		profileUpdate.setActive(true);
		

	}

	@Test
	void testAddTrainee() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		LoginCredentials credentials = new LoginCredentials();
		Mockito.when(traineeService.addTrainee(registrationDetails)).thenReturn(credentials);

		mockMvc.perform(post("/gym/trainee/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registrationDetails))).andExpect(status().isCreated())
				.andReturn();
	}

	@Test
	void testGetTraineeProfile() throws Exception {
		TraineeProfile profile = new TraineeProfile();
		Mockito.when(traineeService.getTraineeProfile(anyString())).thenReturn(profile);

		mockMvc.perform(get("/gym/trainee").param("username", "testUsername")).andExpect(status().isOk()).andReturn();
	}

	@Test
	void testUpdateTraineeProfile() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		Mockito.when(traineeService.updateTraineeProfile(profileUpdate)).thenReturn(traineeProfile);

		mockMvc.perform(put("/gym/trainee").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(profileUpdate))).andExpect(status().isCreated())
		        .andReturn();
	}

	@Test
	void testDeleteTrainee() throws Exception {
		mockMvc.perform(delete("/gym/trainee").param("username", "testUsername")).andExpect(status().isOk())
				.andReturn();
	}

	@Test
	void testGettraineesTrainers() throws Exception {
		List<TrainerDto> trainersList = new ArrayList<>();
		Mockito.when(traineeService.updateTraineeTrainers(anyString(), anyList())).thenReturn(trainersList);

		mockMvc.perform(post("/gym/trainee/trainers").param("username", "testUsername")
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(trainersList)))
				.andExpect(status().isOk()).andReturn();
	}

	@Test
	void testGettraineeTrainings() throws Exception {
		List<TrainingsDetailsResponse> trainingsList = new ArrayList<>(); 
		Mockito.when(traineeService.getTraineeTrainings(trainingsRequestList)).thenReturn(trainingsList);

		mockMvc.perform(post("/gym/trainee/trainings").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(trainingsRequestList))).andExpect(status().isOk())
				.andReturn();
	}

	@Test
	void testGetNotAssaignedTrainers() throws Exception {
		List<TrainerDto> notAssignedTrainers = new ArrayList<>(); 
		Mockito.when(traineeService.getNotAssaignedTrainers(anyString())).thenReturn(notAssignedTrainers);

		mockMvc.perform(get("/gym/trainee/notassaignedtrainers").param("username", "testUsername"))
				.andExpect(status().isOk()).andReturn();
	}
}
