package com.example.one_to_one;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String addressId;

    private String city;

    private String state;

    private String country;

    @OneToOne(mappedBy = "address", cascade = CascadeType.ALL) 
    // In @OneToOne(mappedBy = "address"), it's the inverse side of the relationship — it doesn’t control the cascade, so you manage cascading only from the Student side.
    private Student student;

    /*
    
    Unidirectional Mapping ---> One entity related with another but not vice versa  E.g Student has Address field in table but not vice-versa

    Bidirectional Mapping  ----> Both entity related with one another by @OneToOne mapping. Both table have the other table's PK as their FK
    
    */
}
