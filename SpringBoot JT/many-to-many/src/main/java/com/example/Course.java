package com.example;

import java.util.List;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Course {

    @Id
    private int courseId;
    private String courseName;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER)
    /*
        mappedBy = "courses" It means:

        "This students field is mapped by the courses field in the Student class."

        🔍 It tells Hibernate:

        "Hey, don’t create a new join table for me. The other side (Student.courses) is handling that."

    */

    private List<Student> students;

    /*
    
    Without mappedBy:

    If both Student and Course have @ManyToMany without mappedBy and both define @JoinTable, Hibernate will:

    Create two join tables 

    One from Student → Course

    One from Course → Student

    */

}
