package com.example.Spring.Core;

import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringCoreApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(SpringCoreApplication.class, args);

		Person person = context.getBean(Person.class);
		person.sayHello();

		Student student = context.getBean(Student.class);
		student.sayHello();

	}

}
