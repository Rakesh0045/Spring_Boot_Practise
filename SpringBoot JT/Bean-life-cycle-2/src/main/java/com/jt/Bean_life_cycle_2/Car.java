package com.jt.Bean_life_cycle_2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class Car {

    private Engine engine;

    @Autowired
    public void setEngine(Engine engine) {
        this.engine = engine;
        System.out.println("Dependency of Engine injected into Car");
    }

    @PostConstruct
    public void init() {
        System.out.println("init() method of Car class is called");
    }

    public void runCar() {
        engine.engineStart();
        System.out.println("Car is running");
    }

    @PreDestroy
    public void destroyCar() {
        System.out.println("Car object is destroyed");
    }
}
