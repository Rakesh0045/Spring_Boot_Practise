package com.example.Spring.Core.DependencyInjectionByAutowiring.SetterInjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Car {

    private Engine engine;

    @Autowired
    public Car(Engine engine){
        this.engine = engine;
        System.out.println("Car object is initialized along with wired bean of Engine");
    }

    public void runCar() {
        engine.startEngine();
        System.out.println("Car is running");
    }

}
