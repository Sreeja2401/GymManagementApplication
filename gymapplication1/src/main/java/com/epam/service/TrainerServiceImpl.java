package com.epam.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class TrainerServiceImpl implements TrainerService {

	private final UserRepository userRepository;
	private final TrainerRepository trainerRepository;
	private final TrainingTypeRepository trainingTypeRepository;
	private final Mapper mapper;
	private final KafkaProducer kafkaProducer;
	private final PasswordEncoder passwordEncoder;
	

	@Override
	public LoginCredentials addTrainer(TrainerRegistration trainerRegistration) {
		log.info("inside addTrainer method of TrainerServiceImpl with details : {}", trainerRegistration);

		User user = mapper.userBuilder(trainerRegistration);

		Trainer trainer = new Trainer();
		TrainingType trainingType = trainingTypeRepository.findByTrainingTypeName(trainerRegistration.getTrainingType())
				.orElseThrow(()->new GymException("training type not found"));		
		String userName=mapper.usernameGenerator(trainerRegistration.getFirstName(),trainerRegistration.getLastName());
		String password=mapper.passwordGenerator();
		String encodedPassword= passwordEncoder.encode(password);
		user.setUsername(userName);
		user.setPassword(encodedPassword);
		userRepository.save(user); 
		trainer.setTrainingType(trainingType);
		trainer.setUser(user);
		trainerRepository.save(trainer);
		
		NotificationDto notificationDto=NotificationDto.builder().toEmails(List.of(user.getEmail())).ccEmails(List.of())
				.body("username:" + userName + "\npassword:" + password)
				.subject("registration successfull").build();

		kafkaProducer.sendNotification(notificationDto);

		return LoginCredentials.builder().username(user.getUsername()).password(user.getPassword()).build();

	}

	@Override
	public TrainerProfile getTrainerProfile(String username) {
		log.info("inside addTrainer method of TrainerServiceImpl with details : {}", username);
		Trainer trainer = trainerRepository.findByUserUsername(username)
				.orElseThrow(() -> new GymException("trainer not exist with given username"));

		return mapper.mapTrainerToTrainerProfile(trainer);

	}

	@Override
	@Transactional
	public TrainerProfile updateTrainerProfile(TrainerProfileUpdate profileUpdate) {
		log.info("inside updateTrainerProfile method of TrainerServiceImpl with details : {}", profileUpdate);
		Trainer trainer = trainerRepository.findByUserUsername(profileUpdate.getUsername())
				.orElseThrow(() -> new GymException("user not exist with given username"));
		trainer.getUser().setUsername(profileUpdate.getUsername());
		trainer.getUser().setFirstName(profileUpdate.getFirstName());
		trainer.getUser().setLastName(profileUpdate.getLastName());
		trainer.getUser().setEmail(profileUpdate.getEmail());
		trainer.getUser().setActive(true);

		kafkaProducer.sendNotification(mapper.trainerUpdateNotificationBuilder(trainer));
		return mapper.mapTrainerToTrainerProfile(trainer);

	}

	@Override
	public List<TrainingsDetailsResponse> getTrainerTrainings(TrainerTrainingsRequestList requestList) {
		log.info("inside getTrainerTrainings method of TrainerServiceImpl with details : {}", requestList);
		Trainer trainer = trainerRepository.findByUserUsername(requestList.getUsername())
				.orElseThrow(() -> new GymException("trainer not found"));
		List<Training> trainingsList = trainerRepository.findTrainingsForTrainer(trainer.getUser().getUsername(),
				requestList.getPeriodFrom(), requestList.getPeriodTo(), requestList.getTraineeName());
		log.info("Retriving training details of trainee");
		return mapper.getTrainingDetailsList(trainingsList);

	}

}
