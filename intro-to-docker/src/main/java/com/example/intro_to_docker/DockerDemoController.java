package com.example.intro_to_docker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DockerDemoController {

    @GetMapping("/docker")
    public String getDemo(){
        return "Spring Boot App is Dockerized Successfully";
    }
}
