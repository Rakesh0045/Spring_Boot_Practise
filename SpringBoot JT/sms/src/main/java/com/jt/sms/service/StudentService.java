package com.jt.sms.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.jt.sms.dto.StudentDTO;
import com.jt.sms.entity.Student;
import com.jt.sms.exception.StudentNotFoundException;
import com.jt.sms.repository.StudentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    
   public List<StudentDTO> getStudents() {
    
        List<Student> students = studentRepository.findAll();

        return students.stream().map(student -> {
            StudentDTO dto = new StudentDTO();
            BeanUtils.copyProperties(student, dto);
            return dto;
        }).toList();
}


    public StudentDTO getStudentById(String id){
        var existingStudent = studentRepository.findById(id)
            .orElseThrow(() -> new StudentNotFoundException("Student not found with id: "+id));
        
        var existingStudentDTO = new StudentDTO();
        BeanUtils.copyProperties(existingStudent, existingStudentDTO);

        return existingStudentDTO;
    }

    public StudentDTO saveStudent(Student newStudent) {
        var saveStudent = studentRepository.save(newStudent);
        var studentDTO = new StudentDTO();
        BeanUtils.copyProperties(saveStudent, studentDTO);
        return studentDTO;
    }

    public void deleteStudentById(String id) {
         Student student = studentRepository.findById(id)
        .orElseThrow(() -> new StudentNotFoundException("Student not found with id: " + id));

        studentRepository.delete(student);
    }

    public StudentDTO updateStudentById(String id, StudentDTO studentDTO) {
        //var existingStudent = getStudentById(id);
        // student.setStudentId(id); //Set id to student reference
        Student existingStudent = new Student();
        existingStudent.setStudentId(id);
        BeanUtils.copyProperties(studentDTO, existingStudent);
        var updatedStudent = studentRepository.save(existingStudent); 
        BeanUtils.copyProperties(updatedStudent, studentDTO);
        return studentDTO;
    }
}
