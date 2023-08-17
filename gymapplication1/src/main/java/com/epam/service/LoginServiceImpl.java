package com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.dto.response.LoginCredentials;
import com.epam.entity.User;
import com.epam.exception.GymException;
import com.epam.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	UserRepository userRepository;

	@Transactional
	@Override
	public void updateCredentials(LoginCredentials credentials, String newPassword) {

		log.info("inside updateCredentials method of LoginService with details :{}", credentials);
		
		User user = userRepository.findByUsernameAndPassword(credentials.getUsername(), credentials.getPassword())
				.orElseThrow(() -> new GymException("invalid credentials"));
		user.setPassword(newPassword);
	}

}
