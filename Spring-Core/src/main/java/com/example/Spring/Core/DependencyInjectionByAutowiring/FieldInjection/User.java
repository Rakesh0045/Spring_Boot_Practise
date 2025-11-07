package com.example.Spring.Core.DependencyInjectionByAutowiring.FieldInjection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class User {

    @Autowired  // It gives the instruction to the spring container to assign the address of the Order class bean in the order reference
    public Order order;

    public User(){
        System.out.println("User is initialized");
    }
}

/*

        Marked as @Component so it becomes a Spring Bean.

        Uses Field Injection to inject the Order bean.

        Spring will automatically inject the Order bean into this field by type matching.

        The constructor logs "User is initialized", meaning it runs after the field injection.

 */