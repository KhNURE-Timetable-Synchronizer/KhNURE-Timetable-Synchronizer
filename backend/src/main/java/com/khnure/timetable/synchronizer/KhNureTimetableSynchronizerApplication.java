package com.khnure.timetable.synchronizer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class KhNureTimetableSynchronizerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KhNureTimetableSynchronizerApplication.class, args);
	}
}
