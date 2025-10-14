package com.jt.miscellaneous_topics;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //container gets info that Application contains Bean in this class. Help to register multiple bean
public class AppConfig {

    @Bean // Used for predefined classes or interface
    public CommandLineRunner getCommandLineRunner(){
        return (ss) -> {
            System.out.println("Hello from CommandLine Runner");
            System.out.println("This method is executed just after the container is fully prepared");
        };

        //Implement Lambda expression to return object of CommandLineRunner
    }
}
