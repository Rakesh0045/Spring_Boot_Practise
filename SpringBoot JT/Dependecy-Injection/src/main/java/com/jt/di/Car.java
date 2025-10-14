package com.jt.di;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
// @Scope(value = "prototype")
public class Car {

    // FIELD-BASED INJECTION
    // @Autowired // It gives the instruction to the spring container to assign the address of the Engine class bean in the engine reference

    private Engine engine;

    // SETTER-BASED INJECTION
    // @Autowired
    // public void setEngine(Engine engine) {
    //     System.out.println("Setter method called - DI");
    //     this.engine = engine;
    // }

    //CONSTRUCTOR-BASED INJECTION
    @Autowired //If only 1 constructor is present in class, To implement DI @Autowired is optional
    public Car(Engine engine){
        System.out.println("Constructer called - DI");
        this.engine = engine;
    }

    public Car(){
        //If multiple constructors declared, then spring container cant decide which constructor to call, hence @Autowired is required
    }

    public void runCar() {
        engine.startEngine();
        System.out.println("Car is running");
    }
}
