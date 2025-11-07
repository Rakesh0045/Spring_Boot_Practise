package com.example.Spring.Core.DependencyInjectionByAutowiring.SetterInjection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class,args);
        Car car = context.getBean(Car.class);
        car.runCar();
    }
}

/*

        Spring scans @Component classes: Engine and Car
        Engine bean is created (constructor called)
        Car bean is created:
            Constructor is called
            Engine is injected automatically by Spring
        runCar() method is executed

 */