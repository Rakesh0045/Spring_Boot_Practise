package com.jt.Bean_life_cycle_2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class BeanLifeCycle2Application {

	public static void main(String[] args) {
		System.out.println("main started");
		System.out.println("SpringContainer starts");
		ApplicationContext context = SpringApplication.run(BeanLifeCycle2Application.class, args);
		System.out.println("SpringContainer fully prepared");
		Car car = context.getBean(Car.class);
		car.runCar();
		System.out.println("main end");
	}

}
