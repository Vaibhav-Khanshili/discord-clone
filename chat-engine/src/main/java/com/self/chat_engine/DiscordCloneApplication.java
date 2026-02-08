package com.self.chat_engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DiscordCloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscordCloneApplication.class, args);
	}

}
