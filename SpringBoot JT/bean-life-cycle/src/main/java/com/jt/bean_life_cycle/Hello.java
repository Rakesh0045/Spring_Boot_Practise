package com.jt.bean_life_cycle;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class Hello {
    public Hello(){
        System.out.println("Hello object is constructed");
    }

    @PostConstruct //A method annotated with @PostConstruct is automatically called after dependency injection and before the bean is ready to use.
    public void init(){
        System.out.println("init() method of Hello class is called");
    }

    @PreDestroy
    public void destroyHello(){
        System.out.println("Hello object destroyed");
    }
}
