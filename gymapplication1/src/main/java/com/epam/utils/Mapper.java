package com.epam.utils;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.epam.dto.notification.NotificationDto;
import com.epam.dto.request.TraineeRegistrationDetails;
import com.epam.dto.request.TrainerRegistration;
import com.epam.dto.request.TrainingReportDto;
import com.epam.dto.response.TraineeDto;
import com.epam.dto.response.TraineeProfile;
import com.epam.dto.response.TrainerDto;
import com.epam.dto.response.TrainerProfile;
import com.epam.dto.response.TrainingsDetailsResponse;
import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.Training;
import com.epam.entity.User;

@Component
public class Mapper {

	public User userBuilder(TraineeRegistrationDetails traineeRegistrationDetails) {
		return User.builder().firstName(traineeRegistrationDetails.getFirstName())
				.lastName(traineeRegistrationDetails.getLastName()).createdDate(LocalDate.now())
				.email(traineeRegistrationDetails.getEmail()).isActive(true).build();
	}

	public List<TrainerDto> mapTrainersToTrainerDto(List<Trainer> trainersList) {
		return trainersList.stream().map(trainer -> {
			return TrainerDto.builder().userName(trainer.getUser().getUsername())
					.firstName(trainer.getUser().getFirstName()).lastName(trainer.getUser().getLastName())
					.specialization(trainer.getTrainingType().getTrainingTypeName()).build();
		}).toList();

	}

	public TraineeProfile mapTraineeToTraineeProfile(Trainee trainee) {

		return TraineeProfile.builder().firstName(trainee.getUser().getFirstName())
				.lastName(trainee.getUser().getLastName()).dateOfBirth(trainee.getUser().getTrainee().getDateOfBirth())
				.address(trainee.getUser().getTrainee().getAddress()).isActive(true)
				.trainersList(mapTrainersToTrainerDto(trainee.getUser().getTrainee().getTrainerList())).build();

	}

	public List<TraineeDto> mapTraineeToTraineeDto(List<Trainee> traineeList) {
		return traineeList.stream().map(trainee -> {
			return TraineeDto.builder().userName(trainee.getUser().getUsername())
					.firstName(trainee.getUser().getFirstName()).lastName(trainee.getUser().getLastName()).build();

		}).toList();

	}

	public User userBuilder(TrainerRegistration trainerRegistration) {
		return User.builder().firstName(trainerRegistration.getFirstName()).lastName(trainerRegistration.getLastName())
				.createdDate(LocalDate.now()).email(trainerRegistration.getEmail())
				.isActive(true).build();
	}

	public TrainerProfile mapTrainerToTrainerProfile(Trainer trainer) {

		return TrainerProfile.builder().trainerUsername(trainer.getUser().getUsername())
				.trainerFirstName(trainer.getUser().getFirstName()).trainerLastName(trainer.getUser().getLastName())
				.traineeList(mapTraineeToTraineeDto(trainer.getTraineeList()))
				.trainerSpecialization(trainer.getUser().getTrainer().getTrainingType().getTrainingTypeName())
				.isActive(true).build();

	}

	public List<TrainingsDetailsResponse> getTrainingDetailsList(List<Training> trainingsList) {
		return trainingsList.stream().map(training -> {
			TrainingsDetailsResponse trainingDto = new TrainingsDetailsResponse();
			trainingDto.setTrainingName(training.getTrainingName());
			trainingDto.setTrainingDate(training.getDate());
			trainingDto.setTrainingType(training.getTrainingType().getTrainingTypeName());
			trainingDto.setTrainingDuration(training.getDuration());
			trainingDto.setTrainerName(training.getTrainer().getUser().getUsername());
			trainingDto.setTraineeName(training.getTrainee().getUser().getUsername());
			return trainingDto;
		}).toList();
	}

	public String usernameGenerator(String firstname, String lastname) {

		Random random = new Random();
		String randomNumbers = String.format("%4d", random.nextInt(10000));
		return String.format("%s%s%s", firstname.toLowerCase(), lastname.toLowerCase(), randomNumbers);

	}

	public String passwordGenerator() {
		return new Random().ints(10, 48, 123).filter(i -> (i <= 57 || (i >= 65 && i <= 90) || (i >= 97 && i <= 122)))
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

	}

	public NotificationDto traineeUpdateNotificationBuilder(Trainee trainee) {
		return NotificationDto.builder().toEmails(List.of(trainee.getUser().getEmail())).ccEmails(List.of())
				.body("FirstName:" + trainee.getUser().getFirstName() + "\nLastName:" + trainee.getUser().getLastName()
						+ "\nEmail:" + trainee.getUser().getEmail() + "\nAddress:" + trainee.getAddress()
						+ "\nDate of birth:" + trainee.getDateOfBirth())
				.subject("Details updated").build();
	}

	public NotificationDto trainerUpdateNotificationBuilder(Trainer trainer) {
		return NotificationDto.builder().toEmails(List.of(trainer.getUser().getEmail())).ccEmails(List.of())
				.body("FirstName:" + trainer.getUser().getFirstName() + "\nLastName:" + trainer.getUser().getLastName()
						+ "\nEmail:" + trainer.getUser().getEmail() + "\nSpecialization:"
						+ trainer.getTrainingType().getTrainingTypeName())
				.subject("Details updated").build();
	}

	public NotificationDto trainingNotificationBuilder(Training training) {

		return NotificationDto.builder()
				.toEmails(
						List.of(training.getTrainee().getUser().getEmail(), training.getTrainer().getUser().getEmail()))
				.ccEmails(List.of()).body("TrainingName : " + training.getTrainingName() +

						"\nTraining Date : " + training.getDate() + "\nSpecialization : "
						+ training.getTrainingType().getTrainingTypeName() + "\nTrainerFirstName : "
						+ training.getTrainer().getUser().getFirstName() + "\nTraineeFirstName : "
						+ training.getTrainee().getUser().getFirstName() +

						"\nDuration : " + training.getDuration())
				.subject("training details").build();

	}

	public TrainingReportDto reportBuilder(Training training) {
		TrainingReportDto trainingReport = new TrainingReportDto();
		trainingReport.setTrainerUsername(training.getTrainer().getUser().getUsername());
		trainingReport.setTrainerFirstName(training.getTrainer().getUser().getFirstName());
		trainingReport.setTrainerLastName(training.getTrainer().getUser().getLastName());
		trainingReport.setEmail(training.getTrainer().getUser().getEmail());
		trainingReport.setTrainerStatus(training.getTrainer().getUser().isActive());
		trainingReport.setDate(training.getDate());
		trainingReport.setDuration(training.getDuration());
		return trainingReport;
	}

}