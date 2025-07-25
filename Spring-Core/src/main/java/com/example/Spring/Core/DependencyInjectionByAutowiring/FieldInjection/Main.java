package com.example.Spring.Core.DependencyInjectionByAutowiring.FieldInjection;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(Main.class, args);
        User user = context.getBean(User.class);
    }
}

/*

    | Annotation                   | Purpose                                                                           |
    | ---------------------------- | --------------------------------------------------------------------------------- |
    | `@Component`                 | Marks a class as a Spring Bean (eligible for component scanning)                  |
    | `@Autowired`                 | Injects a bean into another bean by **type** (automatic dependency resolution)    |
    | `@SpringBootApplication`     | Combination of `@Configuration`, `@EnableAutoConfiguration`, and `@ComponentScan` |
    | `SpringApplication.run(...)` | Starts the Spring application and returns the application context                 |


    Flow:

        1. Spring Boot application starts
        2. Component scan picks up:
           - `User` class → registered as a bean
           - `Order` class → registered as a bean
        3. While creating `User`, Spring sees `@Autowired Order order`
           - It finds a matching `Order` bean (by type)
           - Injects the `Order` bean into the `User` bean
        4. Constructors run:
           - "Order is Initialized"
           - "User is initialized"
        5. `User` bean is retrieved in `main()`


    ✅ Field injection is easy and quick, but for better testability, prefer constructor injection.

    ✅ Always annotate your own beans with @Component or use @Bean if the class is external or unmodifiable.


        | Concept                 | Explanation                                                                                   |
        | ----------------------- | --------------------------------------------------------------------------------------------- |
        | Field Injection         | Simplest and most concise, but harder to test or mock                                         |
        | `@Autowired` by default | Works by **type**; throws error if multiple types found (unless `@Qualifier` used)            |
        | Bean Scope              | Both beans here are **singleton** by default                                                  |
        | Order of Initialization | Dependency beans (`Order`) are initialized **before** the class (`User`) that depends on them |



 */
