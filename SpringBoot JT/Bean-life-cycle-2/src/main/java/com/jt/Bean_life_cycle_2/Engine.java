package com.jt.Bean_life_cycle_2;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class Engine {

    public Engine(){
        System.out.println("Engine object is constructed");
    }

    @PostConstruct
    public void init(){
        System.out.println("init() method of Engine class is called");
    }

    public void engineStart(){
        System.out.println("Engine Started");
    }

    @PreDestroy
    public void destroyEngine(){
        System.out.println("Engine object is destroyed");
    }
}
