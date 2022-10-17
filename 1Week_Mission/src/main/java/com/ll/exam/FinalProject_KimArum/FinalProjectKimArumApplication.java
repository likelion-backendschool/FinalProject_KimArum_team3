package com.ll.exam.FinalProject_KimArum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FinalProjectKimArumApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinalProjectKimArumApplication.class, args);
	}

}
