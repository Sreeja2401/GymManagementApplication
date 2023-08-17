package com.epam.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.epam.entity.EmailNotification;

public interface NotificationRepository extends MongoRepository<EmailNotification, Integer>    {

}
