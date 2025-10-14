package com.jt.sms.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jt.sms.dto.StudentDTO;
import com.jt.sms.entity.Student;
import com.jt.sms.service.StudentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/students") // --> Project is developed from scratch
// @RequestMapping("/students") // --> Base mapping: As /students used
// everywhere we took it as base mapping, any further mapping will extend with
// /students

@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    // @GetMapping("/students")
    @GetMapping
    public List<StudentDTO> getStudents() {
        return studentService.getStudents();
    }


    // @GetMapping("/students/{id}")
    @GetMapping("/{id}")
    public StudentDTO getStudent(@PathVariable String id) {
        var existingStudent = studentService.getStudentById(id);
        return studentService.getStudentById(id);
    }

    // @PostMapping("/students")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentDTO createStudent(@Valid @RequestBody StudentDTO newStudentDTO) {
        Student newStudent = new Student();
        BeanUtils.copyProperties(newStudentDTO, newStudent); // Copies StudentDTO obj to Student obj

        //Not recommended as copyProperties can not copy an obj with in the obj
        return studentService.saveStudent(newStudent);
    }

    // @DeleteMapping("/students/delete/{id}")
    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable String id) {
        studentService.deleteStudentById(id);
    }

    // @PutMapping("/students/update/{id}") //PutMapping --> when we have to update the whole resource
    @PutMapping("/update/{id}")
    public StudentDTO updateStudent(@PathVariable String id, @RequestBody StudentDTO studentDTO) {
        return studentService.updateStudentById(id, studentDTO);
    }
}
