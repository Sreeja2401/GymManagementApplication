package com.epam.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.dto.request.TrainingDetails;
import com.epam.service.TrainingServiceImpl;

@RestController
@RequestMapping("/gym/training")
public class TrainingRestController {
	
	@Autowired
	TrainingServiceImpl trainingServiceImpl;
	
	
	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void addTraining(TrainingDetails trainingDetails) {
		trainingServiceImpl.addTraining(trainingDetails);
	}

}
