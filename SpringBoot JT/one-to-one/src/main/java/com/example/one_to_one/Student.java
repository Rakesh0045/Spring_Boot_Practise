package com.example.one_to_one;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int studentRoll;

    private String studentName;

    private String studentEmail;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY) //Maps Primary Key of Address Table to Student Table as Foreign Key
    
    //CascadeType.MERGE ensures that when you update (merge) the Student, the associated Address is also automatically updated in the database.
    
    //We can also use CascadeType.ALL instead of using one by one

    @JoinColumn(name = "address_id") // change name from address_address_id to address_id
    private Address address; //Acts as Foreign Key in Student Table address_address_id
}

/*

Relationship           Fetch type
------------           ----------

One-to-One    ------>    EAGER

Many-to-One   ------>    EAGER

One-to-Many   ------>    LAZY

Many-to-Many  ------>    LAZY



* Fetch type determines whether when an Entity is extracted then whether its associated/related entities are associated or not

* Eager means the associated Entities are extracted

* Lazy means the associated Entities are not extracted


*/