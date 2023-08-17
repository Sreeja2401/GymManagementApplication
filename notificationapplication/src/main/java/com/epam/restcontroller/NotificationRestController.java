package com.epam.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.dto.notification.NotificationDto;
import com.epam.service.NotificationService;

@RestController
public class NotificationRestController {
	
	@Autowired
	NotificationService notificationService;
	
	@PostMapping("/notification")
	@ResponseStatus(code=HttpStatus.OK )
    public void sendNotification(@RequestBody NotificationDto dto)
    {
		notificationService.sendNotification(dto);
    }
	
}
