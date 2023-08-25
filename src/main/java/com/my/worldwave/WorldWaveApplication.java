package com.my.worldwave;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WorldWaveApplication {

	public static void main(String[] args) {
		SpringApplication.run(WorldWaveApplication.class, args);
	}

}
