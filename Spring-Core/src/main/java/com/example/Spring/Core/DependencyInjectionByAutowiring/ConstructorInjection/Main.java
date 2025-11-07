package com.example.Spring.Core.DependencyInjectionByAutowiring.ConstructorInjection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class,args);
        Car car = context.getBean(Car.class);
        car.runCar();
    }
}

/*


        | Concept                      | Description                                                                   |
        | ---------------------------- | ----------------------------------------------------------------------------- |
        | **Setter Injection**         | Spring injects dependencies using a setter method annotated with `@Autowired` |
        | `@Component`                 | Marks the class as a Spring-managed bean                                      |
        | `@Autowired` on Setter       | Instructs Spring to inject the dependency into the setter method              |
        | `@SpringBootApplication`     | Enables component scanning, autoconfiguration, and config support             |
        | `SpringApplication.run(...)` | Bootstraps the Spring application and returns the `ApplicationContext`        |


       Workflow:

            1. Spring Boot application starts
            2. Component scan detects:
               - Car class → marked as @Component → creates Car bean
               - Engine class → marked as @Component → creates Engine bean
            3. Car constructor runs → "Car object is initialized"
            4. Setter method runs:
               - "Setter method called - DI"
               - Spring injects Engine bean via setEngine()
            5. Engine constructor runs → "Engine object is initialized"
            6. runCar() is called:
               - engine.startEngine() → "Engine is started"
               - "Car is running"


        🔁 Bean Lifecycle in Spring (Simplified)

            For a class like Car or Engine, Spring follows this general lifecycle:

            Class is detected during component scanning (@Component)
            Spring creates the object → calls the constructor
            Then, Spring injects dependencies
                Via setter (@Autowired on setter)
                Or via field (@Autowired on field)
                Or via constructor (@Autowired on constructor)
            Spring completes bean initialization and makes it ready for use.




        📌 Best Practices

        ✅ Prefer constructor injection for required dependencies.
        ✅ Use setter injection for optional or mutable dependencies.
        ✅ Avoid public fields with @Autowired unless it’s absolutely necessary (less testable and clean).






 */
