package com.epam.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class TraineeServiceImpl implements TraineeService {

	private final TraineeRepository traineeRepository;
	private final TrainingRepository trainingRepository;
	private final TrainerRepository trainerRepository;
	private final Mapper mapper;
	private final KafkaProducer kafkaProducer;
	private final PasswordEncoder encoder;

	private static final String GYM_EXCEPTION_MESSAGE = "trainee not found with given username";

	@Override
	public LoginCredentials addTrainee(TraineeRegistrationDetails traineeRegistrationDetails) {

		log.info("inside addTrainee method of TraineeServiceImpl with details : {}", traineeRegistrationDetails);

		User user = mapper.userBuilder(traineeRegistrationDetails);
		String userName=mapper.usernameGenerator(traineeRegistrationDetails.getFirstName(),traineeRegistrationDetails.getLastName());
		String password=mapper.passwordGenerator();
		String encodedPassword= encoder.encode(password);
		user.setUsername(userName);
		user.setPassword(encodedPassword);
		Trainee trainee = Trainee.builder().address(traineeRegistrationDetails.getAddress())
				.dateOfBirth(traineeRegistrationDetails.getDateOfBirth()).user(user).build();
		traineeRepository.save(trainee);

		NotificationDto notificationDto=NotificationDto.builder().toEmails(List.of(user.getEmail())).ccEmails(List.of())
				.body("username:" + userName + "\npassword:" + password)
				.subject("registration successfull").build();
		
		kafkaProducer.sendNotification(notificationDto);

		return LoginCredentials.builder().username(userName).password(password).build();
	}

	@Override
	public TraineeProfile getTraineeProfile(String username) {
		log.info("inside getTraineeProfile method of TraineeServiceImpl with details : {}", username);
		Trainee trainee = traineeRepository.findByUserUsername(username)
				.orElseThrow(() -> new GymException(GYM_EXCEPTION_MESSAGE));
		return mapper.mapTraineeToTraineeProfile(trainee);

	}

	@Override
	@Transactional
	public TraineeProfile updateTraineeProfile(TraineeProfileUpdate detailsToBeUpdated) {

		Trainee trainee = traineeRepository.findByUserUsername(detailsToBeUpdated.getUsername())
				.orElseThrow(() -> new GymException(GYM_EXCEPTION_MESSAGE));
		trainee.getUser().setUsername(detailsToBeUpdated.getUsername());
		trainee.getUser().setFirstName(detailsToBeUpdated.getFirstName());
		trainee.getUser().setLastName(detailsToBeUpdated.getLastName());
		trainee.getUser().setEmail(detailsToBeUpdated.getEmail());
		trainee.setDateOfBirth(detailsToBeUpdated.getDateOfBirth());
		trainee.setAddress(detailsToBeUpdated.getAddress());
		trainee.getUser().isActive();

		kafkaProducer.sendNotification(mapper.traineeUpdateNotificationBuilder(trainee));
		return mapper.mapTraineeToTraineeProfile(trainee);

	}

	@Override
	public void deleteTrainee(String username) {
		log.info("inside method of deleteTrainee in TraineeServiceImpl with usrname :{}", username);
		Trainee trainee = traineeRepository.findByUserUsername(username)
				.orElseThrow(() -> new GymException(GYM_EXCEPTION_MESSAGE));

		traineeRepository.deleteById(trainee.getId());

	}

	@Override
	@Transactional
	public List<TrainerDto> updateTraineeTrainers(String username, List<String> trainersUsernameList) {
		log.info("inside method of updateTraineeTrainers in TraineeServiceImpl with username :{}", username);
		Trainee trainee = traineeRepository.findByUserUsername(username)
				.orElseThrow(() -> new GymException(GYM_EXCEPTION_MESSAGE));

		List<Trainer> trainersToAdd = trainersUsernameList.stream().map(trainerRepository::findByUserUsername)
				.filter(Optional::isPresent).filter(trainer -> !trainee.getTrainerList().contains(trainer.get()))
				.map(Optional::get).toList();

		List<Trainer> trainersToRemove = trainee.getTrainerList().stream()
				.filter(trainer -> !trainersUsernameList.contains(trainer.getUser().getUsername())).toList();

		trainee.getTrainerList().addAll(trainersToAdd);
		trainee.getTrainerList().removeAll(trainersToRemove);
        //
		List<Training> trainingToRemove = trainingRepository.findByTraineeAndTrainerNotIn(trainee,
				trainee.getTrainerList());
		trainingRepository.deleteAll(trainingToRemove);
        //
		return mapper.mapTrainersToTrainerDto(trainee.getTrainerList());
	}

	@Override
	public List<TrainerDto> getNotAssaignedTrainers(String username) {
		log.info("inside method of updateTraineeTrainers in TraineeServiceImpl with username :{}", username);
		Trainee trainee = traineeRepository.findByUserUsername(username)
				.orElseThrow(() -> new GymException(GYM_EXCEPTION_MESSAGE));

		List<Trainer> trainerList = trainerRepository.findByTraineeListNotContaining(trainee);
		List<Trainer> notAssaignedActiveTrainers = trainerList.stream().filter(t -> t.getUser().isActive()).toList();
		return mapper.mapTrainersToTrainerDto(notAssaignedActiveTrainers);

	}

	@Override
	public List<TrainingsDetailsResponse> getTraineeTrainings(TraineeTrainingsRequestList traineetrainings) {
		log.info("inside method of updateTraineeTrainers in TraineeServiceImpl with username :{}",
				traineetrainings.getUsername());
		Trainee trainee = traineeRepository.findByUserUsername(traineetrainings.getUsername())
				.orElseThrow(() -> new GymException(GYM_EXCEPTION_MESSAGE));
		List<Training> trainingsList = traineeRepository.findTrainingsForTrainee(trainee.getUser().getUsername(),
				traineetrainings.getPeriodFrom(), traineetrainings.getPeriodTo(), traineetrainings.getTrainerName(),
				traineetrainings.getTrainingType());
		return mapper.getTrainingDetailsList(trainingsList);

	}
}
