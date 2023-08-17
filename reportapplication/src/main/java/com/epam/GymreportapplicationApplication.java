package com.epam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GymreportapplicationApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymreportapplicationApplication.class, args);
	}

}
