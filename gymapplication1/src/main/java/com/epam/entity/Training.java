package com.epam.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String trainingName;
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    private Long duration;
    
    @ManyToOne
    private Trainee trainee;
    
    @ManyToOne
    private TrainingType trainingType;
    
    @ManyToOne
    private Trainer trainer;

}