package com.example.one_to_one;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class OneToOneApplication {

	private final StudentRepository studentRepository;
	private final AddressRepository addressRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(OneToOneApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(){
		return val -> {
			Student student = Student.builder()
								.studentName("Rakesh")
								.studentEmail("r@gmail.com")
								.build();


			Address address = Address.builder()
								.city("Cuttack")
								.state("Odisha")
								.country("India")
								.build();


			//FIX 1: either save addresss first then save student
			//addressRepository.save(address);

			//FIX2: cascade = CascadeType.PERSIST is used which specifies that before Student object is stored then its associated Address object is also stored so we dont explicitly have to save the Address obj before Student

			//PROBLEM - Student obj is inserted before Address obj he	nce address_id can't be fetched					
			student.setAddress(address);
			studentRepository.save(student);



			// Student existingStudent = studentRepository.findById(2).orElseThrow();
			// existingStudent.setStudentName("Rakesh Kumar Parida");
			// existingStudent.getAddress().setCity("BBSR"); //Extract the Address object for the Student and change its City
			//studentRepository.save(existingStudent);


			//studentRepository.delete(existingStudent); //--> Deletes Student obj row but does not delete address, After using CascadeType.Remove the corresponding address of Student obj is also deleted from Address Table









			//RETRIEVE

			// Student existingStudent = studentRepository.findById(3).orElseThrow();
			// System.out.println(existingStudent.getStudentRoll());
			// System.out.println(existingStudent.getStudentName());
			// System.out.println(existingStudent.getStudentEmail());
			
			// Address existingStudentAddress = existingStudent.getAddress();
			// System.out.println(existingStudentAddress.getCity());
			// System.out.println(existingStudentAddress.getCountry());
			// System.out.println(existingStudentAddress.getState());


			//RETRIEVE STUDENT FROM ADDRESS

			// Address exisitingAddress = addressRepository.findById("3c4151ce-f2d1-4170-a8c2-2b0f19573bff").orElseThrow();
			// Student existingAddressStudent = exisitingAddress.getStudent();
			// System.out.println(existingAddressStudent.getStudentName());
			// System.out.println(existingAddressStudent.getStudentRoll());
			// System.out.println(existingAddressStudent.getStudentEmail());
			
			// System.out.println(exisitingAddress.getCity());
			// System.out.println(exisitingAddress.getState());
			// System.out.println(exisitingAddress.getCountry());






			Student student2 = Student.builder()
								.studentName("Rakesh1")
								.studentEmail("r1@gmail.com")
								.build();

			Address address2 = Address.builder()	
								.city("Cuttack")
								.state("Odisha")
								.country("India")
								.student(student2) //Storing Student obj in Address
								.build();

			//addressRepository.save(address2);	
			student2.setAddress(address2);
			studentRepository.save(student2);				
			
			/*
			
			Owning side --> contain foreign key (Student)

			Inverse side --> whose PK is present in other table as FK (Address)
			
			Always Save through JPA from Owning side otherwise FK will not be mapped and FK column for inserted row will be empty

			*/

		};
	}

}
