package com.epam.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.epam.entity.TrainingType;



@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType, Integer>{

	boolean existsByTrainingTypeName(String trainingType);

	Optional<TrainingType> findByTrainingTypeName(String trainingType);

}