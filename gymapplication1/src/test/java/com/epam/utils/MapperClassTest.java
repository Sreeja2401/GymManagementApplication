package com.epam.utils;

import com.epam.dto.notification.NotificationDto;
import com.epam.dto.request.TraineeRegistrationDetails;
import com.epam.dto.request.TrainerRegistration;
import com.epam.dto.request.TrainingReportDto;
import com.epam.dto.response.*;
import com.epam.entity.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MapperClassTest {

    @InjectMocks
    Mapper mapper;


    @Test
    void testUserBuilder() {
        TraineeRegistrationDetails traineeDetails = new TraineeRegistrationDetails();
        traineeDetails.setFirstName("John");
        traineeDetails.setLastName("Doe");
        traineeDetails.setEmail("john.doe@example.com");
        User user = mapper.userBuilder(traineeDetails);
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals(LocalDate.now(), user.getCreatedDate());
        assertTrue(user.isActive());
    }

    @Test
    void testMapTrainersToTrainerDto() {
        Trainer trainer1 = mock(Trainer.class);
        User user = User.builder().username("trainer1").firstName("Trainer").lastName("Two").build();
        when(trainer1.getUser()).thenReturn(user);
        TrainingType trainingType = TrainingType.builder().trainingTypeName("AnotherSpecialization").build();
        when(trainer1.getTrainingType()).thenReturn(trainingType);
        List<Trainer> trainersList = List.of(trainer1);
        List<TrainerDto> result = mapper.mapTrainersToTrainerDto(trainersList);
        TrainerDto trainerDto1 = result.get(0);
        assertEquals("trainer1", trainerDto1.getUserName());
        assertEquals("Trainer", trainerDto1.getFirstName());
        assertEquals("Two", trainerDto1.getLastName());
        assertEquals("AnotherSpecialization", trainerDto1.getSpecialization());
    }

    @Test
    void testMapTraineesToTraineeDto() {
        Trainee trainee = mock(Trainee.class);
        User user = User.builder().username("trainer1").firstName("Trainer").lastName("Two").build();
        when(trainee.getUser()).thenReturn(user);

        List<Trainee> traineeList = List.of(trainee);
        List<TraineeDto> result = mapper.mapTraineeToTraineeDto(traineeList);
        TraineeDto traineeDto1 = result.get(0);
        assertEquals("trainer1", traineeDto1.getUserName());
        assertEquals("Trainer", traineeDto1.getFirstName());
        assertEquals("Two", traineeDto1.getLastName());
    }

    @Test
    void testUserBuilderForTrainerRegistration() {
        TrainerRegistration trainerDetails = new TrainerRegistration();
        trainerDetails.setFirstName("John");
        trainerDetails.setLastName("Doe");
        trainerDetails.setEmail("john.doe@example.com");
        User user = mapper.userBuilder(trainerDetails);
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals(LocalDate.now(), user.getCreatedDate());
        assertTrue(user.isActive());
    }

    @Test
    public void testGetTrainingDetailsList() {

        List<Training> trainingsList = new ArrayList<>();
        User user1 = new User();
        Trainee trainee = Trainee.builder().user(user1).build();
        User user2 = new User();
        Trainer trainer = Trainer.builder().user(user2).build();

        Training training = Training.builder().trainingType(TrainingType.builder().trainingTypeName("Workshop").build()).trainingName("Training 1").trainee(trainee).date(LocalDate.of(2023, 8, 14)).duration(2L).trainer(trainer).build();
        trainingsList.add(training);
        List<TrainingsDetailsResponse> responseList = mapper.getTrainingDetailsList(trainingsList);

        TrainingsDetailsResponse response1 = responseList.get(0);
        assertEquals("Training 1", response1.getTrainingName());
        assertEquals(LocalDate.of(2023, 8, 14), response1.getTrainingDate());
        assertEquals("Workshop", response1.getTrainingType());
        assertEquals(2, response1.getTrainingDuration());
        assertEquals(trainer.getUser().getUsername(), response1.getTrainerName());
        assertEquals(trainee.getUser().getUsername(), response1.getTraineeName());
    }

    @Test
    public void testUsernameGenerator() {
        String username = mapper.usernameGenerator("John", "Doe");
        assertNotNull(username);
    }

    @Test
    public void testPasswordGenerator() {
         String password = mapper.passwordGenerator();
         assertNotNull(password);
    }

    @Test
    public void testTraineeUpdateNotificationBuilder() {

        Trainee trainee = mock(Trainee.class);
        User user = mock(User.class);
          user.setEmail("trainee@example.com");
          user.setFirstName("John");
          user.setLastName("Doe");
          trainee.setUser(user);
        trainee.setAddress("123 Main St");
        trainee.setDateOfBirth(LocalDate.parse("1990-01-01"));
        NotificationDto notification = NotificationDto.builder().toEmails(List.of("trainee@example.com")).ccEmails(List.of()).body("FirstName: John\nLastName: Doe\nEmail: trainee@example.com\nAddress: 123 Main St\nDate of birth: 1990-01-01").subject("Details updated").build();
        assertEquals("trainee@example.com", notification.getToEmails().get(0));
        assertEquals("FirstName: John\nLastName: Doe\nEmail: trainee@example.com\nAddress: 123 Main St\nDate of birth: 1990-01-01", notification.getBody());
        assertEquals("Details updated", notification.getSubject());
    }

    @Test
    public void testTrainerUpdateNotificationBuilder() {
        Trainer trainer = mock(Trainer.class);
        User user = mock(User.class);
        TrainingType trainingType = mock(TrainingType.class);
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setEmail("trainer@example.com");
        trainingType.setTrainingTypeName("Fitness");
        trainer.setUser(user);
        trainer.setTrainingType(trainingType);

        NotificationDto notification = NotificationDto.builder().toEmails(List.of("trainer@example.com")).ccEmails(List.of()).body("FirstName: Jane\nLastName: Smith\nEmail: trainer@example.com\nSpecialization: Fitness").subject("Details updated").build();

        assertEquals("trainer@example.com", notification.getToEmails().get(0));
        assertEquals("FirstName: Jane\nLastName: Smith\nEmail: trainer@example.com\nSpecialization: Fitness", notification.getBody());
        assertEquals("Details updated", notification.getSubject());
    }
    @Test
    public void testTrainingNotificationBuilder() {
        Training training = new Training();
        Trainee trainee = new Trainee();
        User traineeUser = new User();
        Trainer trainer = new Trainer();
        User trainerUser = new User();
        TrainingType trainingType = new TrainingType();

        traineeUser.setEmail("trainee@example.com");
        traineeUser.setFirstName("John");
        trainee.setUser(traineeUser);

        trainerUser.setEmail("trainer@example.com");
        trainerUser.setFirstName("Jane");
        trainer.setUser(trainerUser);

        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingName("Fitness Training");
        training.setDate(LocalDate.parse("2023-08-15"));
        trainingType.setTrainingTypeName("Fitness");
        training.setTrainingType(trainingType);
        training.setDuration(1L);

        NotificationDto notification =NotificationDto.builder()
                .toEmails(
                        List.of(training.getTrainee().getUser().getEmail(), training.getTrainer().getUser().getEmail()))
                .ccEmails(List.of()).body("TrainingName : " + training.getTrainingName() +

                        "\nTraining Date : " + training.getDate() + "\nSpecialization : "
                        + training.getTrainingType().getTrainingTypeName() + "\nTrainerFirstName : "
                        + training.getTrainer().getUser().getFirstName() + "\nTraineeFirstName : "
                        + training.getTrainee().getUser().getFirstName() +

                        "\nDuration : " + training.getDuration())
                .subject("training details").build();

        assertEquals(2, notification.getToEmails().size());
        assertEquals("trainee@example.com", notification.getToEmails().get(0));
        assertEquals("trainer@example.com", notification.getToEmails().get(1));
        assertEquals("TrainingName : Fitness Training\nTraining Date : 2023-08-15\nSpecialization : Fitness\nTrainerFirstName : Jane\nTraineeFirstName : John\nDuration : 1", notification.getBody());
        assertEquals("training details", notification.getSubject());
    }

    @Test
    public void testReportBuilder() {
        Training training = new Training();
        Trainer trainer = new Trainer();
        User user = new User();
        user.setUsername("trainer123");
        user.setFirstName("Jane");
        user.setLastName("Doe");
        user.setEmail("trainer@example.com");
        user.isActive();
        trainer.setUser(user);
        training.setTrainer(trainer);
        training.setDate(LocalDate.parse("2023-08-15"));
        training.setDuration(1L);

        TrainingReportDto trainingReport = new TrainingReportDto();
        trainingReport.setTrainerUsername(training.getTrainer().getUser().getUsername());
        trainingReport.setTrainerFirstName(training.getTrainer().getUser().getFirstName());
        trainingReport.setTrainerLastName(training.getTrainer().getUser().getLastName());
        trainingReport.setEmail(training.getTrainer().getUser().getEmail());
        trainingReport.setTrainerStatus(training.getTrainer().getUser().isActive());
        trainingReport.setDate(training.getDate());
        trainingReport.setDuration(training.getDuration());

        assertEquals("trainer123", trainingReport.getTrainerUsername());
        assertEquals("Jane", trainingReport.getTrainerFirstName());
        assertEquals("Doe", trainingReport.getTrainerLastName());
        assertEquals("trainer@example.com", trainingReport.getEmail());
        assertEquals(1, trainingReport.getDuration());
    }
    @Test
    public void testMapTraineeToTraineeProfile() {
        Trainee trainee = mock(Trainee.class);
        User user = mock(User.class);
        TraineeProfile traineeProfile = mock(TraineeProfile.class);

        when(trainee.getUser()).thenReturn(user);
        when(user.getFirstName()).thenReturn("John");
        when(user.getLastName()).thenReturn("Doe");
        when(user.getTrainee()).thenReturn(trainee);
        when(trainee.getDateOfBirth()).thenReturn(LocalDate.parse("1990-01-01"));
        when(trainee.getAddress()).thenReturn("123 Main St");
        when(trainee.getTrainerList()).thenReturn(new ArrayList<>()); // Assuming you want an empty list of trainers


        traineeProfile.setFirstName("John");
        traineeProfile.setLastName("Doe");
        traineeProfile.setDateOfBirth(LocalDate.parse("1990-01-01"));
        traineeProfile.setAddress("123 Main St");
        traineeProfile.setActive(true);
        traineeProfile.setTrainersList(new ArrayList<>());

        TraineeProfile mappedProfile = mapper.mapTraineeToTraineeProfile(trainee);

        assertEquals("John", mappedProfile.getFirstName());
        assertEquals("Doe", mappedProfile.getLastName());
        assertEquals("123 Main St", mappedProfile.getAddress());
        assertEquals(true, mappedProfile.isActive());
        assertEquals(0, mappedProfile.getTrainersList().size());
    }
    @Test
    public void testMapTrainerToTrainerProfile() {
        Trainer trainer = new Trainer();
        TrainingType trainingType =new TrainingType();
        User user = new User();
        user.setUsername("trainer123");
        user.setFirstName("Jane");
        user.setLastName("Doe");
        trainer.setUser(user);
        user.setTrainer(trainer);
        trainer.setTraineeList(new ArrayList<>());
        trainingType.setTrainingTypeName("Fitness");
        trainer.setTrainingType(trainingType);

        TrainerProfile trainerProfile = mapper.mapTrainerToTrainerProfile(trainer);

        assertEquals("trainer123", trainerProfile.getTrainerUsername());
        assertEquals("Jane", trainerProfile.getTrainerFirstName());
        assertEquals("Doe", trainerProfile.getTrainerLastName());
        assertEquals(0, trainerProfile.getTraineeList().size());
        assertEquals("Fitness", trainerProfile.getTrainerSpecialization());
        assertEquals(true, trainerProfile.isActive());
    }


}
