package com.epam.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.epam.dto.response.LoginCredentials;
import com.epam.entity.User;
import com.epam.exception.GymException;
import com.epam.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
 class LoginServiceTest {
	
	@Mock
	UserRepository userRepository;
	
	@InjectMocks
	LoginServiceImpl loginServiceImpl;
	
	LoginCredentials credentials= LoginCredentials.builder()
			.username("sreeja").password("ertyuikjxn").build();
	
	
	@Test
	void updateCredentialsTest()
	{
		Mockito.when(userRepository.findByUsernameAndPassword(anyString(), anyString()))
		.thenReturn(Optional.of(new User()));
		loginServiceImpl.updateCredentials(credentials, "erghjkxbx");
		Mockito.verify(userRepository,Mockito.times(1)).findByUsernameAndPassword(anyString(), anyString());
	}
	@Test
	void updateCredentialsExceptionCaseTest()
	{
		Mockito.when(userRepository.findByUsernameAndPassword(anyString(), anyString()))
		.thenReturn(Optional.empty());
		assertThrows(GymException.class, ()->loginServiceImpl.updateCredentials(credentials, "erghjkxbx"));
		Mockito.verify(userRepository,Mockito.times(1)).findByUsernameAndPassword(anyString(), anyString());
	}
	

}
