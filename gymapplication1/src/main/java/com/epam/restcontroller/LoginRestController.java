package com.epam.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.dto.response.LoginCredentials;
import com.epam.service.LoginService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/gym/login")
public class LoginRestController {
	@Autowired
	LoginService loginService;
	
	/*
	 * @PostMapping("/authentication")
	 * 
	 * @ResponseStatus(code = HttpStatus.OK) public void
	 * userAuthenticatio(@Valid @RequestBody LoginCredentials credentials) { log.
	 * info("inside userAuthenticatio method of LoginRestController with details :{}"
	 * ,credentials); loginService.userAuthentication(credentials); }
	 */
	
	@PostMapping("/update")
	@ResponseStatus(code = HttpStatus.OK)
	public void updateCredentials(@Valid @RequestBody LoginCredentials credentials,@RequestParam String newPassword)
	{
		log.info("inside updateCredentials method of LoginRestController with details :{}",credentials,newPassword);
		loginService.updateCredentials(credentials,newPassword);
	}
	

}
