package com.epam.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.epam.dto.request.TrainerProfileUpdate;
import com.epam.dto.request.TrainerRegistration;
import com.epam.dto.request.TrainerTrainingsRequestList;
import com.epam.dto.response.LoginCredentials;
import com.epam.dto.response.TrainerProfile;
import com.epam.dto.response.TrainingsDetailsResponse;
import com.epam.entity.Trainer;
import com.epam.entity.Training;
import com.epam.entity.TrainingType;
import com.epam.entity.User;
import com.epam.exception.GymException;
import com.epam.kafka.KafkaProducer;
import com.epam.repository.TrainerRepository;
import com.epam.repository.TrainingTypeRepository;
import com.epam.repository.UserRepository;
import com.epam.utils.Mapper;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

	@Mock
	private TrainerRepository trainerRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private KafkaProducer kafkaProducer;
	@Mock
	private TrainingTypeRepository trainingTypeRepository;

	@Mock
	PasswordEncoder encoder;

	@Mock
	Mapper mapper;

	@InjectMocks
	private TrainerServiceImpl trainerService;

	TrainerRegistration registrationDetails;
	Trainer trainer;
	User user;
	TrainerProfileUpdate profileUpdate;
	TrainerTrainingsRequestList requestList;

	@BeforeEach
	void setUp() {
		trainer = new Trainer();
		user = new User();
		registrationDetails = new TrainerRegistration();
		registrationDetails.setEmail("sreejamangarapu@gmail.com");
		registrationDetails.setFirstName("sreeja");
		registrationDetails.setLastName("mangarapu");
		registrationDetails.setTrainingType("fitness");

		profileUpdate = new TrainerProfileUpdate();
		profileUpdate.setUsername("sreeja");
		profileUpdate.setFirstName("sreeja");
		profileUpdate.setLastName("mangarapu");
		profileUpdate.setEmail("sreejamangarapu@gmail.com");
		profileUpdate.setSpectialization("Zumba");
		profileUpdate.setActive(true);

		requestList = new TrainerTrainingsRequestList();
		requestList.setUsername("sreeja");

	}

	@Test
	void testAddTrainer() {
		user.setEmail(registrationDetails.getEmail());
		TrainingType trainingType = new TrainingType();
		Mockito.when(mapper.userBuilder(registrationDetails)).thenReturn(user);
		Mockito.when(trainingTypeRepository.findByTrainingTypeName(registrationDetails.getTrainingType()))
				.thenReturn(Optional.of(trainingType));
		Mockito.when(mapper.usernameGenerator(anyString(), anyString())).thenReturn("sreejamanga478");
		Mockito.when(mapper.passwordGenerator()).thenReturn("du2fs78xb");
		Mockito.when(encoder.encode(anyString())).thenReturn("2345678uihjbvcxshgx");
		Mockito.when(userRepository.save(user)).thenReturn(user);
		Mockito.when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);
		Mockito.doNothing().when(kafkaProducer).sendNotification(any(NotificationDto.class));
		LoginCredentials credentials = trainerService.addTrainer(registrationDetails);
		assertNotNull(credentials);

	}

	@Test
	void testAddTrainerExceptionCase() {
		Mockito.when(mapper.userBuilder(registrationDetails)).thenReturn(user);
		Mockito.when(trainingTypeRepository.findByTrainingTypeName(registrationDetails.getTrainingType()))
				.thenReturn(Optional.empty());
		assertThrows(GymException.class, () -> trainerService.addTrainer(registrationDetails));
	}

	@Test
	void testGetTrainerProfile() {
		String username = "testUser";
		TrainerProfile trainerProfile = new TrainerProfile();
		when(trainerRepository.findByUserUsername(username)).thenReturn(Optional.of(trainer));
		when(mapper.mapTrainerToTrainerProfile(trainer)).thenReturn(trainerProfile);
		TrainerProfile result = trainerService.getTrainerProfile(username);
		assertNotNull(result);

	}

	@Test
	void testGetTrainerProfileExceptionCase() {
		String username = "testUser";
		when(trainerRepository.findByUserUsername(username)).thenReturn(Optional.empty());
		assertThrows(GymException.class, () -> trainerService.getTrainerProfile(username));
	}

	@Test
	void testUpdateTrainerProfile() {
		trainer.setUser(user);
		when(trainerRepository.findByUserUsername(profileUpdate.getUsername())).thenReturn(Optional.of(trainer));
		when(mapper.trainerUpdateNotificationBuilder(trainer)).thenReturn(new NotificationDto());
		when(mapper.mapTrainerToTrainerProfile(trainer)).thenReturn(new TrainerProfile());
		TrainerProfile result = trainerService.updateTrainerProfile(profileUpdate);
		assertNotNull(result);
		verify(kafkaProducer).sendNotification(any());
	}

	@Test
	void testUpdateTrainerProfileException() {
		when(trainerRepository.findByUserUsername(profileUpdate.getUsername())).thenReturn(Optional.empty());
		assertThrows(GymException.class,()->trainerService.updateTrainerProfile(profileUpdate) );
		
	}

	@Test
	void testGetTrainerTrainings() {

		user.setUsername("sreeja");
		trainer.setUser(user);
		when(trainerRepository.findByUserUsername(requestList.getUsername())).thenReturn(Optional.of(trainer));
		List<Training> trainingsList = new ArrayList<>();
		when(trainerRepository.findTrainingsForTrainer(eq(requestList.getUsername()), any(), any(), any()))
				.thenReturn(trainingsList);
		List<TrainingsDetailsResponse> result = trainerService.getTrainerTrainings(requestList);
		assertNotNull(result);
	}

	
	@Test
	void testGetTrainerTrainingsExceptionCase() {
		TrainerTrainingsRequestList requestList = new TrainerTrainingsRequestList();
		requestList.setUsername("sreeja");
		when(trainerRepository.findByUserUsername(requestList.getUsername())).thenReturn(Optional.empty());
		assertThrows(GymException.class, () -> trainerService.getTrainerTrainings(requestList));

	}
}
