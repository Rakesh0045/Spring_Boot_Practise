package com.jt.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.Employee;

@SpringBootApplication
@ComponentScan(basePackages = "com")
public class HelloApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(HelloApplication.class, args);
		Person person = context.getBean(Person.class);
		person.sayHello();

		Employee employee = context.getBean(Employee.class);
		employee.sayHello();

		Student student = context.getBean(Student.class);
		student.sayHello();

		Teacher teacher = context.getBean(Teacher.class);
		teacher.sayHello();
	}

}
