package com.epam.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.dto.request.TrainerProfileUpdate;
import com.epam.dto.request.TrainerRegistration;
import com.epam.dto.request.TrainerTrainingsRequestList;
import com.epam.dto.response.LoginCredentials;
import com.epam.dto.response.TrainerProfile;
import com.epam.dto.response.TrainingsDetailsResponse;
import com.epam.service.TrainerServiceImpl;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/gym/trainer")
@Slf4j
public class TrainerRestController {

	@Autowired
	TrainerServiceImpl trainerServiceImpl;
	

	@PostMapping("/register")
	public ResponseEntity<LoginCredentials> addTrainer(@Valid @RequestBody TrainerRegistration trainerRegistration) {
		log.info("inside addTrainer method of TrainerRestController with details :{}",trainerRegistration);
		return new ResponseEntity<>(trainerServiceImpl.addTrainer(trainerRegistration), HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<TrainerProfile> getTrainerProfile(@RequestParam String username) {
		log.info("inside getTrainerProfile method of TrainerRestController with details :{}",username);
		return new ResponseEntity<>(trainerServiceImpl.getTrainerProfile(username), HttpStatus.OK);

	}

	@PutMapping
	public ResponseEntity<TrainerProfile> updateTrainerProfile(@Valid @RequestBody TrainerProfileUpdate profileUpdate) {
		log.info("inside updateTrainerProfile method of TrainerRestController with details :{}",profileUpdate);
		return new ResponseEntity<>(trainerServiceImpl.updateTrainerProfile(profileUpdate), HttpStatus.CREATED);

	}


	@PostMapping("/trainings")
	public ResponseEntity<List<TrainingsDetailsResponse>> getTrainerTrainings(@Valid @RequestBody TrainerTrainingsRequestList trainings) {
		log.info("inside gettrainersTrainings method of TrainerRestController with details :{}",trainings);
		return new ResponseEntity<>(trainerServiceImpl.getTrainerTrainings(trainings), HttpStatus.OK);
	}
    
	/*
	 * @GetMapping("/trainingReport") public ResponseEntity<ResponseReportDto>
	 * getTrainingReport(@RequestParam String userName) throws Exception { return
	 * new
	 * ResponseEntity<>(proxy.getTrainingReport(userName).getBody(),HttpStatus.OK);
	 * 
	 * }
	 */
}
