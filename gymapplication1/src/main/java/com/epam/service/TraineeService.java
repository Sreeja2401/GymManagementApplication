package com.epam.service;

import java.util.List;

import com.epam.dto.request.TraineeProfileUpdate;
import com.epam.dto.request.TraineeRegistrationDetails;
import com.epam.dto.request.TraineeTrainingsRequestList;
import com.epam.dto.response.LoginCredentials;
import com.epam.dto.response.TraineeProfile;
import com.epam.dto.response.TrainerDto;
import com.epam.dto.response.TrainingsDetailsResponse;

import jakarta.validation.Valid;

public interface TraineeService {

	

	void deleteTrainee(String userName);

	LoginCredentials addTrainee(TraineeRegistrationDetails trainee);

	TraineeProfile getTraineeProfile(String username);

	TraineeProfile updateTraineeProfile(@Valid TraineeProfileUpdate profileUpdate);
	
	List<TrainerDto> getNotAssaignedTrainers(String username);

	List<TrainingsDetailsResponse> getTraineeTrainings(TraineeTrainingsRequestList traineetrainings);

	List<TrainerDto> updateTraineeTrainers(String username, List<String> trainersList);
}
