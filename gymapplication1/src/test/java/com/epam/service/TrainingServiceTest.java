package com.epam.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.dto.notification.NotificationDto;
import com.epam.dto.request.TrainingDetails;
import com.epam.dto.request.TrainingReportDto;
import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.Training;
import com.epam.exception.GymException;
import com.epam.kafka.KafkaProducer;
import com.epam.repository.TraineeRepository;
import com.epam.repository.TrainerRepository;
import com.epam.repository.TrainingRepository;
import com.epam.utils.Mapper;

@ExtendWith(MockitoExtension.class)
public class TrainingServiceTest {
	@Mock
	TraineeRepository traineeRepository;
	@Mock
	TrainerRepository trainerRepository;
	@Mock
	TrainingRepository trainingRepository;
	@Mock
	KafkaProducer kafkaProducer;
	@Mock
	Mapper mapper;

	@InjectMocks
	TrainingServiceImpl trainingServiceImpl;

	TrainingDetails trainingDetails;
	Trainee trainee;
	Trainer trainer;
	Training training;
	NotificationDto notificationDto;

	@BeforeEach
	void setUp() {
		trainingDetails = new TrainingDetails();
		trainingDetails.setTraineeUsername("sreeja");
		trainingDetails.setTrainerUsername("Hari");
		trainingDetails.setTrainingDate(LocalDate.of(2023, 04, 23));
		trainingDetails.setTrainingDuration(5L);
		trainingDetails.setTrainingName("fitnessTraining");
		trainingDetails.setTrainingType("fitness");
		trainee = new Trainee();
		trainer = new Trainer();
		training = new Training();
		trainee.setTrainerList(List.of(trainer));

		notificationDto = new NotificationDto();

	}

	@Test
	void addTrainingTest() {

		Mockito.when(traineeRepository.findByUserUsername(trainingDetails.getTraineeUsername()))
				.thenReturn(Optional.of(trainee));
		Mockito.when(trainerRepository.findByUserUsername(trainingDetails.getTrainerUsername()))
				.thenReturn(Optional.of(trainer));
		Mockito.when(trainingRepository.save(any(Training.class))).thenReturn(training);
		Mockito.when(mapper.trainingNotificationBuilder(any(Training.class))).thenReturn(notificationDto);
		Mockito.doNothing().when(kafkaProducer).sendNotification(any(NotificationDto.class));
		Mockito.when(mapper.reportBuilder(any(Training.class))).thenReturn(new TrainingReportDto());
		Mockito.doNothing().when(kafkaProducer).sendReport(any(TrainingReportDto.class));
		trainingServiceImpl.addTraining(trainingDetails);

		Mockito.verify(traineeRepository, times(1)).findByUserUsername(trainingDetails.getTraineeUsername());
		Mockito.verify(trainerRepository, times(1)).findByUserUsername(trainingDetails.getTrainerUsername());
		Mockito.verify(trainingRepository, times(1)).save(any(Training.class));

	}
	@Test
	void addTrainingTestWhenTraineeNotPresent() {

		Mockito.when(traineeRepository.findByUserUsername(trainingDetails.getTraineeUsername()))
				.thenReturn(Optional.empty());
		assertThrows(GymException.class,()->trainingServiceImpl.addTraining(trainingDetails));
		Mockito.verify(traineeRepository, times(1)).findByUserUsername(trainingDetails.getTraineeUsername());
		
	}
	@Test
	void addTrainingTestWhenTrainerNotPresent() {
		Mockito.when(traineeRepository.findByUserUsername(trainingDetails.getTraineeUsername()))
		.thenReturn(Optional.of(trainee));
		Mockito.when(trainerRepository.findByUserUsername(trainingDetails.getTrainerUsername()))
				.thenReturn(Optional.empty());
		assertThrows(GymException.class,()->trainingServiceImpl.addTraining(trainingDetails));
		Mockito.verify(traineeRepository, times(1)).findByUserUsername(trainingDetails.getTraineeUsername());
		Mockito.verify(trainerRepository, times(1)).findByUserUsername(trainingDetails.getTrainerUsername());
		
	}
	@Test
	void addTrainingTestWhenTraineeTrainerNotAssociated() {
		Trainer trainer2=new Trainer();
		Mockito.when(traineeRepository.findByUserUsername(trainingDetails.getTraineeUsername()))
		.thenReturn(Optional.of(trainee));
		Mockito.when(trainerRepository.findByUserUsername(trainingDetails.getTrainerUsername()))
				.thenReturn(Optional.of(trainer2));
		assertThrows(GymException.class,()->trainingServiceImpl.addTraining(trainingDetails));
		Mockito.verify(traineeRepository, times(1)).findByUserUsername(trainingDetails.getTraineeUsername());
		Mockito.verify(trainerRepository, times(1)).findByUserUsername(trainingDetails.getTrainerUsername());
		
	}

}
