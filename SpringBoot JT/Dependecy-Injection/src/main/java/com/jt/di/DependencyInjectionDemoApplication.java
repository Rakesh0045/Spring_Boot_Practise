package com.jt.di;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DependencyInjectionDemoApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DependencyInjectionDemoApplication.class, args);
		Car car = context.getBean(Car.class);
		// Car car2 = context.getBean(Car.class);

		System.out.println(car.hashCode());
		// System.out.println(car2.hashCode());

		car.runCar();
		// Hashcode value is same as both refs point to same obj. Car is a singleton
		// class by default
	}

}
