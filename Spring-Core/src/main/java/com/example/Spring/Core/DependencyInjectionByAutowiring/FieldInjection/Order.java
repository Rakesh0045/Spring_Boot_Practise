package com.example.Spring.Core.DependencyInjectionByAutowiring.FieldInjection;

import org.springframework.stereotype.Component;

@Component
public class Order {
    public Order(){
        System.out.println("Order is Initialized");
    }
}

/*

    This class is a Spring-managed bean thanks to @Component.

    On app start, Spring creates a singleton Order bean.


 */