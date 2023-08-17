package com.epam.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.epam.dto.notification.NotificationDto;
import com.epam.dto.request.TraineeProfileUpdate;
import com.epam.dto.request.TraineeRegistrationDetails;
import com.epam.dto.request.TraineeTrainingsRequestList;
import com.epam.dto.response.LoginCredentials;
import com.epam.dto.response.TraineeProfile;
import com.epam.dto.response.TrainerDto;
import com.epam.dto.response.TrainingsDetailsResponse;
import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.Training;
import com.epam.entity.User;
import com.epam.exception.GymException;
import com.epam.kafka.KafkaProducer;
import com.epam.repository.TraineeRepository;
import com.epam.repository.TrainerRepository;
import com.epam.repository.TrainingRepository;
import com.epam.utils.Mapper;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

	@InjectMocks
	TraineeServiceImpl traineeService;

	@Mock
	TraineeRepository traineeRepository;
	@Mock
	TrainingRepository trainingRepository;
	@Mock
	TrainerRepository trainerRepository;
	@Mock
	Mapper mapper;
	@Mock
	KafkaProducer kafkaProducer;
	@Mock
	PasswordEncoder encoder;
	TraineeRegistrationDetails registrationDetails;
	User user;
	Trainee trainee;
	TraineeProfile profile;
	TraineeProfileUpdate profileUpdate;

	@BeforeEach
	void setUp() {
		registrationDetails = new TraineeRegistrationDetails();
		registrationDetails.setFirstName("Sreeja");
		registrationDetails.setLastName("Mangarapu");
		registrationDetails.setEmail("sreeja@gmail.com");
		registrationDetails.setDateOfBirth(LocalDate.of(2001, 2, 1));
		registrationDetails.setAddress("Nirmal");

		user = new User();
		trainee = new Trainee();
		trainee.setUser(user);
		profile = new TraineeProfile();

		profileUpdate = new TraineeProfileUpdate();
		profileUpdate.setUsername("testUser");
		profileUpdate.setFirstName("NewFirstName");
		profileUpdate.setLastName("NewLastName");
		profileUpdate.setEmail("newemail@example.com");
		profileUpdate.setDateOfBirth(LocalDate.of(1990, 1, 1));
		profileUpdate.setAddress("NewAddress");
	}

	@Test
	void testAddTrainee() {

		user.setEmail(registrationDetails.getEmail());

		when(mapper.userBuilder(registrationDetails)).thenReturn(user);
		when(mapper.usernameGenerator(anyString(), anyString())).thenReturn("generatedUsername");
		when(mapper.passwordGenerator()).thenReturn("generatedPassword");
		when(encoder.encode(anyString())).thenReturn("encodedPassword");
		when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);
		Mockito.doNothing().when(kafkaProducer).sendNotification(any(NotificationDto.class));

		LoginCredentials credentials = traineeService.addTrainee(registrationDetails);
		assertNotNull(credentials);
		assertEquals("generatedUsername", credentials.getUsername());
		assertEquals("generatedPassword", credentials.getPassword());

	}

	@Test
	void testGetTraineeProfile() {
		String username = "testUsername";
		Mockito.when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.of(trainee));
		Mockito.when(mapper.mapTraineeToTraineeProfile(trainee)).thenReturn(profile);
		TraineeProfile result = traineeService.getTraineeProfile(username);
		assertEquals(profile, result);
		Mockito.verify(traineeRepository, Mockito.times(1)).findByUserUsername(username);
	}

	@Test
	void testGetTraineeProfileExceptionCase() {
		String username = "testUsername";
		Mockito.when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.empty());
		assertThrows(GymException.class, () -> traineeService.getTraineeProfile(username));
	}

	@Test
	void testUpdateTraineeProfile() {
		
		when(traineeRepository.findByUserUsername(profileUpdate.getUsername())).thenReturn(Optional.of(trainee));
		when(mapper.traineeUpdateNotificationBuilder(trainee)).thenReturn(new NotificationDto());
		when(mapper.mapTraineeToTraineeProfile(trainee)).thenReturn(new TraineeProfile());

		TraineeProfile updatedProfile = traineeService.updateTraineeProfile(profileUpdate);

		assertNotNull(updatedProfile);
		assertEquals(profileUpdate.getUsername(), trainee.getUser().getUsername());
		assertEquals(profileUpdate.getFirstName(), trainee.getUser().getFirstName());
		assertEquals(profileUpdate.getLastName(), trainee.getUser().getLastName());
		assertEquals(profileUpdate.getEmail(), trainee.getUser().getEmail());
		assertEquals(profileUpdate.getDateOfBirth(), trainee.getDateOfBirth());
		assertEquals(profileUpdate.getAddress(), trainee.getAddress());

		verify(kafkaProducer, times(1)).sendNotification(any(NotificationDto.class));
		verify(traineeRepository, times(1)).findByUserUsername(profileUpdate.getUsername());
		verify(mapper, times(1)).traineeUpdateNotificationBuilder(trainee);
		verify(mapper, times(1)).mapTraineeToTraineeProfile(trainee);
	}

	@Test
	void testUpdateTraineeProfileExceptionCase() {
		
		when(traineeRepository.findByUserUsername(profileUpdate.getUsername())).thenReturn(Optional.empty());
		assertThrows(GymException.class, ()->traineeService.updateTraineeProfile(profileUpdate));
		
	}

	@Test
	void testDeleteTrainee() {
		String username = "testUser";
		when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.of(trainee));
		traineeService.deleteTrainee(username);
		verify(traineeRepository, times(1)).findByUserUsername(username);
		verify(traineeRepository, times(1)).deleteById(trainee.getId());
	}

	@Test
	void testDeleteTraineeExceptionCase() {
		String username = "testUser";
		when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.empty());
		assertThrows(GymException.class, () -> traineeService.deleteTrainee(username));

	}

	@Test
	void testUpdateTraineeTrainers() {

		String username = "testUser";

		List<String> trainersUsernameList = new ArrayList<>();
		trainersUsernameList.add("trainer1");
		trainersUsernameList.add("trainer2");

		User user1 = new User();
		User user2 = new User();
		Trainee trainee = new Trainee();
		Trainer trainer1 = new Trainer();
		trainer1.setUser(user1);
		Trainer trainer2 = new Trainer();
		trainer2.setUser(user2);

		List<Trainer> trainersList = new ArrayList<>();
		trainersList.add(trainer1);
		trainee.setTrainerList(trainersList);

		when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.of(trainee));
		when(trainerRepository.findByUserUsername("trainer1")).thenReturn(Optional.of(trainer1));
		when(trainerRepository.findByUserUsername("trainer2")).thenReturn(Optional.of(trainer2));

		List<TrainerDto> result = traineeService.updateTraineeTrainers(username, trainersUsernameList);

		verify(traineeRepository, times(1)).findByUserUsername(username);
		verify(trainerRepository, times(1)).findByUserUsername("trainer1");
		verify(trainerRepository, times(1)).findByUserUsername("trainer2");

	}

	@Test
	void testUpdateTraineeTrainersExceptionCase() {

		String username = "testUser";
		List<String> trainersUsernameList = new ArrayList<>();
		trainersUsernameList.add("trainer1");
		trainersUsernameList.add("trainer2");
		when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.empty());
		assertThrows(GymException.class, () -> traineeService.updateTraineeTrainers(username, trainersUsernameList));
		verify(traineeRepository, times(1)).findByUserUsername(username);

	}

	@Test
	void testGetNotAssaignedTrainers() {

		String username = "testUser";
		User user1 = new User();
		Trainee trainee = new Trainee();
		trainee.setUser(user1);
		List<Trainer> trainerList = new ArrayList<>();
		Trainer trainer1 = new Trainer();
		User user2 = new User();
		User user3 = new User();
		trainer1.setUser(user2);
		Trainer trainer2 = new Trainer();
		trainer2.setUser(user3);
		trainerList.add(trainer1);
		trainerList.add(trainer2);

		when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.of(trainee));
		when(trainerRepository.findByTraineeListNotContaining(trainee)).thenReturn(trainerList);

		List<TrainerDto> result = traineeService.getNotAssaignedTrainers(username);

		verify(traineeRepository, times(1)).findByUserUsername(username);
		verify(trainerRepository, times(1)).findByTraineeListNotContaining(trainee);

	}

	@Test
	void testGetNotAssaignedTrainersExceptionCase() {

		String username = "testUser";
		when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.empty());
		assertThrows(GymException.class, () -> traineeService.getNotAssaignedTrainers(username));
		verify(traineeRepository, times(1)).findByUserUsername(username);
	}

	@Test
	void testGetTraineeTrainings() {
		String username = "testUser";
		TraineeTrainingsRequestList requestList=new TraineeTrainingsRequestList();
		requestList.setUsername(username);
		Trainee trainee = new Trainee();
		User user1 = new User();
		user1.setUsername("testUser");
		trainee.setUser(user1);

		List<Training> trainingsList = new ArrayList<>();
		Training training1 = new Training();
		trainingsList.add(training1);

		when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.of(trainee));
		when(traineeRepository.findTrainingsForTrainee(eq(username), any(), any(), any(), any())).thenReturn(trainingsList);

		List<TrainingsDetailsResponse> result = traineeService
				.getTraineeTrainings(requestList);

		verify(traineeRepository, times(1)).findByUserUsername(username);

	}

	@Test
	void testGetTraineeTrainingsExceptionCase() {
		String username = "testUser";
		TraineeTrainingsRequestList requestList=new TraineeTrainingsRequestList();
		requestList.setUsername(username);
		when(traineeRepository.findByUserUsername(username)).thenReturn(Optional.empty());
		assertThrows(GymException.class, () -> traineeService
				.getTraineeTrainings(requestList));
		verify(traineeRepository, times(1)).findByUserUsername(username);

	}
}
