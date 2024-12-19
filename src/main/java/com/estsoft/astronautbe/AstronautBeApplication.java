package com.estsoft.astronautbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
// @PropertySource("classpath:env.properties")
public class AstronautBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(AstronautBeApplication.class, args);
	}

}
