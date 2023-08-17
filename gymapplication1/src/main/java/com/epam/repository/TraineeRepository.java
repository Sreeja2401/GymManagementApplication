package com.epam.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.epam.entity.Trainee;
import com.epam.entity.Training;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee, Integer>{
	Optional<Trainee> findByUserId(int userId);

	Optional<Trainee> findByUserUsername(String username);
	
	
	@Query("SELECT t FROM Training t " +
            "JOIN t.trainee tr " +
            "JOIN t.trainer tnr " +
            "JOIN t.trainingType tt " +
            "WHERE tr.user.username = :username " +
            "AND (:periodFrom IS NULL OR t.date >= :periodFrom) " +
            "AND (:periodTo IS NULL OR t.date <= :periodTo) " +
            "AND (:trainerName IS NULL OR tnr.user.username LIKE %:trainerName%) " +
            "AND (:trainingType IS NULL OR tt.trainingTypeName LIKE %:trainingType%)")
	List<Training> findTrainingsForTrainee(String username, LocalDate periodFrom, LocalDate periodTo, String trainerName, String trainingType);
}

