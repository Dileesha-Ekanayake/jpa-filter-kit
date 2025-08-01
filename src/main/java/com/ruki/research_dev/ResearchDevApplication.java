package com.ruki.research_dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ResearchDevApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResearchDevApplication.class, args);
	}

}
