package com.example.Spring.Core.DependencyInjectionByAutowiring.ConstructorInjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Car {

    private Engine engine;

     @Autowired
     public void setEngine(Engine engine) {
         System.out.println("Setter method called - DI");
         this.engine = engine;
     }

    public Car(){
        System.out.println("Car object is initialized");
    }

    public void runCar() {
        engine.startEngine();
        System.out.println("Car is running");
    }

}

/*

        | Step | Action                                                               |
        | ---- | -------------------------------------------------------------------- |
        | 1️⃣  | Spring detects `@Component` class                                    |
        | 2️⃣  | Spring calls the constructor (`new Car()`)                           |
        | 3️⃣  | Spring injects dependencies using `@Autowired` (setter in your case) |
        | 4️⃣  | Bean is fully initialized and stored in context                      |

 */
