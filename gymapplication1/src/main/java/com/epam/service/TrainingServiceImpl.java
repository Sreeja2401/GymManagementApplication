package com.epam.service;

import org.springframework.stereotype.Service;

import com.epam.dto.request.TrainingDetails;
import com.epam.entity.Trainee;
import com.epam.entity.Trainer;
import com.epam.entity.Training;
import com.epam.exception.GymException;
import com.epam.kafka.KafkaProducer;
import com.epam.repository.TraineeRepository;
import com.epam.repository.TrainerRepository;
import com.epam.repository.TrainingRepository;
import com.epam.utils.Mapper;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TrainingServiceImpl implements TrainingService {

	private final TraineeRepository traineeRepository;
	private final TrainerRepository trainerRepository;
	private final TrainingRepository trainingRepository;
	private final KafkaProducer kafkaProducer;
	private final Mapper mapper;

	@Override
	public void addTraining(TrainingDetails trainingDetails) {

		Trainee trainee = traineeRepository.findByUserUsername(trainingDetails.getTraineeUsername())
				.orElseThrow(() -> new GymException("trainee not found"));

		Trainer trainer = trainerRepository.findByUserUsername(trainingDetails.getTrainerUsername())
				.orElseThrow(() -> new GymException("trainer not found"));

		if (!trainee.getTrainerList().contains(trainer))
		{
			throw new GymException("trainer is not associated with trainee !!");
		}

		Training training = Training.builder().date(trainingDetails.getTrainingDate())
				.duration(trainingDetails.getTrainingDuration()).trainingName(trainingDetails.getTrainingName())
				.trainee(trainee).trainer(trainer).trainingType(trainer.getTrainingType()).build();
		trainingRepository.save(training);


		kafkaProducer.sendNotification(mapper.trainingNotificationBuilder(training));
		kafkaProducer.sendReport(mapper.reportBuilder(training));


	}
}
