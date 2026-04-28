package com.example.auth_practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AuthPracticeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthPracticeApplication.class, args);
	}

}
