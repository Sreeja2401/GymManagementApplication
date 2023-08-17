package com.epam.service;

import java.util.List;

import com.epam.dto.request.TrainerProfileUpdate;
import com.epam.dto.request.TrainerRegistration;
import com.epam.dto.request.TrainerTrainingsRequestList;
import com.epam.dto.response.LoginCredentials;
import com.epam.dto.response.TrainerProfile;
import com.epam.dto.response.TrainingsDetailsResponse;

public interface TrainerService {

	

	LoginCredentials addTrainer(TrainerRegistration trainerRegistration);
		
	TrainerProfile getTrainerProfile(String username);

	TrainerProfile updateTrainerProfile(TrainerProfileUpdate profileUpdate);
	
	List<TrainingsDetailsResponse> getTrainerTrainings(TrainerTrainingsRequestList requestList);

	
}
