package com.example.Spring.Core;

import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringCoreApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(SpringCoreApplication.class, args);

		System.out.println("Spring context initialized successfully");

		System.out.println("Fetching GreetingsService bean from context...");
		GreetingsService greetingsService = context.getBean(GreetingsService.class);

		System.out.println("GreetingsService bean fetched successfully. Calling method...");
		greetingsService.sayHello();
	}

}
