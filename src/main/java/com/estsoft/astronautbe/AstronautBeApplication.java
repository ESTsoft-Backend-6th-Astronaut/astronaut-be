package com.estsoft.astronautbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AstronautBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AstronautBeApplication.class, args);
	}

}
