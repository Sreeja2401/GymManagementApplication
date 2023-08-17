package com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.epam.dto.notification.NotificationDto;
import com.epam.entity.EmailNotification;
import com.epam.repository.NotificationRepository;

@Service
public class NotificationService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private NotificationRepository notificationRepository;

	
	public void sendNotification(NotificationDto dto) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("mangarapusreeja@gmail.com");
        mailMessage.setTo(dto.getToEmails().toArray(new String[0]));
        mailMessage.setCc(dto.getCcEmails().toArray(new String[0]));
        mailMessage.setSubject(dto.getSubject());
        mailMessage.setText(dto.getBody());

        EmailNotification notification = EmailNotification.builder().fromEmail("mangarapusreeja@gmail.com").toEmails(dto.getToEmails())
                .ccEmails(dto.getCcEmails()).body(dto.getBody()).build();
        try {
            javaMailSender.send(mailMessage);
            notification.setStatus("Mail Sent");
            notification.setRemarks("Mail sent successfully!!!");
        } catch (MailException e) {
            notification.setStatus("Mail failed");
            notification.setRemarks("Failed to send a mail " + e.getMessage());
        }
        notificationRepository.save(notification);

    }

	
}
