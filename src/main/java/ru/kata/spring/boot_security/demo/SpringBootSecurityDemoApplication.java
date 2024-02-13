package ru.kata.spring.boot_security.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {

	public static void main(String[] args) {
		Logger logger = Logger.getLogger(SpringBootSecurityDemoApplication.class.getName());

		SpringApplication.run(SpringBootSecurityDemoApplication.class, args);

		logger.info("Приложение запустилось. Перейдите по адресу http://localhost:8080/user");
	}
}
