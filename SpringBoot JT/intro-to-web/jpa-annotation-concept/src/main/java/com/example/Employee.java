package com.example;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Builder;
import lombok.Data;

@Data  //getter,setter,tostring,requiredargs
@Entity
@Table(name="employees_table")
@Builder
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID ) //Universal Unique Identifier ---> generates string of 36 char unique id
    private String employeeId;

    @Column(name = "emp_name", columnDefinition = "VARCHAR(30)",nullable = false)
    //insertable=false, updatable=false, unique = true
    private String employeeName;

    @Lob //String - TEXT, byte[] --> BLOB, char[] --> CLOB
    @Column(columnDefinition="MEDIUMTEXT")
    private String employeeDescription;

    @Transient //does not allow to store the field as column in DB
    private double salary;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus employeeStatus;

    @CreationTimestamp
    private LocalDateTime createdDateTime;

    @UpdateTimestamp
    private LocalDateTime updatedDateTime;

}
