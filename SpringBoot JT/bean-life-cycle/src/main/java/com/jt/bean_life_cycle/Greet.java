package com.jt.bean_life_cycle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class Greet {

    public Greet(){
        System.out.println("Greet Object is constructed");
    }

    @Autowired //It injects the required bean or object of Greeting class here
    public void setGreeting(Greeting greeting){
        System.out.println("Depenedency Injected");
    }

    /* 
    
    A method annotated with @PostConstruct is automatically called after dependency injection and before the bean is ready to use i.e bean usage
    
    ➡️ The method marked with @PostConstruct runs after Spring has finished injecting all dependencies (i.e., after all @Autowired fields or methods are processed),

    ➡️ but before the bean is actually returned to the user or added fully into the Spring container for general use.

     */

    @PostConstruct
    public void init(){
        System.out.println("init() method of Greet class is called");
    }

    public void sayHello(){
        System.out.println("Hello from Greet");
    }

    @PreDestroy
    public void destroyGreet(){
        System.out.println("Greet object destroyed");
    }

}
