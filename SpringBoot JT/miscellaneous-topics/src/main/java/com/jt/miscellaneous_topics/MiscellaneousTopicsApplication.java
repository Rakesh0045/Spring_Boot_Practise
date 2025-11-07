package com.jt.miscellaneous_topics;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MiscellaneousTopicsApplication { //implements CommandLineRunner 

	private Animal animal;

	//Qualifier specifies which class ref to use to call eat() method 
	public MiscellaneousTopicsApplication(@Qualifier("cat") Animal animal){
		this.animal = animal;
	}

	public static void main(String[] args) {
		SpringApplication.run(MiscellaneousTopicsApplication.class, args);
		System.out.println("sdja");
		//animal.eat(); Cannot make a static reference to the non-static field animal hence CommandLineRunner is useful
	}

	// @Override
	// public void run(String... args){
	// 	System.out.println("Hello from CommandLine runner");
	// 	System.out.println("This method is executed just after the container is fully prepared");
	// }

	@Bean // Used for predefined classes or interface
	public CommandLineRunner commandLineRunner(){
		return args -> {
			System.out.println("Hello from CommandLine runner");
			System.out.println("This method is executed just after the container is fully prepared");
			animal.eat();
		};
	}

}

/**
 * commandline-runner
 * interface beans concept
 */
