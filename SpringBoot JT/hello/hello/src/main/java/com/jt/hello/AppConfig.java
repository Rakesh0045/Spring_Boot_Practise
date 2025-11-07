package com.jt.hello;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // It is another way to tell the Spring that some Beans are registered within
               // this file
public class AppConfig {

    @Bean
    public Teacher getTeacher() {
        System.out.println("Factory method and returns Teacher bean");
        return new Teacher();
    }
}
