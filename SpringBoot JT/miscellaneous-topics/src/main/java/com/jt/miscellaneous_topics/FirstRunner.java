package com.jt.miscellaneous_topics;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class FirstRunner implements CommandLineRunner{

    //It is a child class of CommandLineRunner so we have to use @Component. so that container manage the bean of First runner and invokes run() 
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello from CommandLine runner");
	    System.out.println("This method is executed just after the container is fully prepared");
    }
    
}
