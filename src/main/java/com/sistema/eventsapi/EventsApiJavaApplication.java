package com.sistema.eventsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.sistema.eventsapi")
public class EventsApiJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventsApiJavaApplication.class, args);
	}
}
