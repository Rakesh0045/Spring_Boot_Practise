package com.example.Spring.Core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration //✅ @Configuration: Marks this class as a source of Spring bean definitions
public class AppConfig {

    @Bean //✅ @Bean: Registers the return object as a bean into the Spring container
    public GreetingsService greetingsService(){
        return new GreetingsService();
    }

    /*

    @SpringBootApplication → triggers @ComponentScan
       │
       ▼
    Scans com.example.Spring.Core.*
       │
       ├── Finds AppConfig (@Configuration)
       │      └── Calls greetingsService() → registers as Bean
       │
       └── Finds GreetingsService when you ask for it in main method


    | Question                                         | Answer                                                                          |
    | ------------------------------------------------ | ------------------------------------------------------------------------------- |
    | Why is the `@Bean` method in `AppConfig` called? | Because Spring Boot uses `@ComponentScan`, which finds `@Configuration` classes |
    | Do I need to explicitly register `AppConfig`?    | **No**, as long as it's in the same package or subpackage as your main class    |
    | What if it's in a different package?             | Use `@ComponentScan(basePackages = "...")` to tell Spring where to look         |



    */


    //Registering Bean of CalculatorService and MessageService

    @Bean
    public CalculatorService calculatorService(){
        return new CalculatorService();
    }

    @Bean
    public MessageService messageService(){
        return new MessageService();
    }



}
