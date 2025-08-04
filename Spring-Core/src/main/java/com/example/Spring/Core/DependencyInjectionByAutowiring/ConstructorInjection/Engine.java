package com.example.Spring.Core.DependencyInjectionByAutowiring.ConstructorInjection;

import org.springframework.stereotype.Component;

@Component
public class Engine {

    public Engine(){
        System.out.println("Engine object is initialized");
    }

    public void startEngine(){
        System.out.println("Engine is started");
    }

}
