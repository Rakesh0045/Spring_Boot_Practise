package com.example.intro_to_rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

// @RestController
@Controller
@RequiredArgsConstructor
public class StudentController {

    private final ObjectMapper objectMapper;
    
    @GetMapping("/student")
    @ResponseBody //when we want to return a data/object rather than html file name
    public Student getStudent(){
        return new Student(101, "Rakesh");
    }


    /*
    
    * When Java backend send code to Javascript or any other frontend & vice-versa it shares data as JSON object 

    * Jackson converts Java obj to JSON obj and JSON to Java

    * @ResponseBody --> without this Jackson will not work

    * @RestController contains @ResponseBody so when we use @RestController we dont need to write @ResponseBody
    
    
    */



    // INTERNAL WORKING OF CONVERSION OF JAVA OBJECT TO JSON OBJECT
    @GetMapping("/student2")
    @ResponseBody
    public String getStudent2() throws JsonProcessingException{
        var student = new Student(102, "Sanoj");
        String json = objectMapper.writeValueAsString(student);
        return json;
    }




    // CONVERSION JSON ---> JAVA
    @GetMapping("/student3")
    @ResponseBody
    public Student getStudent3() throws JsonProcessingException{
        String json = """
                {
                    "roll":103,
                    "name":"Manoj"

                }
                """;
        Student jsonStudent = objectMapper.readValue(json, Student.class);
        System.out.println(jsonStudent);
        return jsonStudent;
    }
    
    
}
