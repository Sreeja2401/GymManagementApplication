package com.epam.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.dto.request.TraineeProfileUpdate;
import com.epam.dto.request.TraineeRegistrationDetails;
import com.epam.dto.request.TraineeTrainingsRequestList;
import com.epam.dto.response.LoginCredentials;
import com.epam.dto.response.TraineeProfile;
import com.epam.dto.response.TrainerDto;
import com.epam.dto.response.TrainingsDetailsResponse;
import com.epam.service.TraineeServiceImpl;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/gym/trainee")
public class TraineeRestController {

    @Autowired
    TraineeServiceImpl traineeServiceImpl;


    @PostMapping("/register")
    public ResponseEntity<LoginCredentials> addTrainee(@Valid @RequestBody TraineeRegistrationDetails traineeRegistration) {
        log.info("inside add trainee method with trainee details:{}", traineeRegistration);
        LoginCredentials credentials = traineeServiceImpl.addTrainee(traineeRegistration);
        return new ResponseEntity<>(credentials, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<TraineeProfile> getTraineeProfile(@RequestParam String username) {
        return new ResponseEntity<>(traineeServiceImpl.getTraineeProfile(username), HttpStatus.OK);

    }

    @PutMapping
    public ResponseEntity<TraineeProfile> updateTraineeProfile(@Valid @RequestBody TraineeProfileUpdate profileUpdate) {
        return new ResponseEntity<>(traineeServiceImpl.updateTraineeProfile(profileUpdate), HttpStatus.CREATED);

    }

    @DeleteMapping
    @ResponseStatus(code = HttpStatus.OK)
    public void deleteTrainee(@RequestParam String username) {
        traineeServiceImpl.deleteTrainee(username);
    }

    @PostMapping("/trainers")
    public ResponseEntity<List<TrainerDto>> getTraineesTrainers(@RequestParam String username, @Valid @RequestBody List<String> trainersList) {
        return new ResponseEntity<>(traineeServiceImpl.updateTraineeTrainers(username, trainersList), HttpStatus.OK);
    }

    @PostMapping("/trainings")
    public ResponseEntity<List<TrainingsDetailsResponse>> getTraineeTrainings(@Valid @RequestBody TraineeTrainingsRequestList trainings) {
        return new ResponseEntity<>(traineeServiceImpl.getTraineeTrainings(trainings), HttpStatus.OK);
    }

    @GetMapping("/notassaignedtrainers")
    public ResponseEntity<List<TrainerDto>> getNotAssaignedTrainers(@RequestParam String username) {
        return new ResponseEntity<>(traineeServiceImpl.getNotAssaignedTrainers(username), HttpStatus.OK);
    }


}
