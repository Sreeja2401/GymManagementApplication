package com.epam.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.epam.entity.TrainingsSummary;

public interface TrainingsSummaryRepository extends MongoRepository<TrainingsSummary, String> {
	
	
}
