package com.example.many_to_one;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class ManyToOneApplication implements CommandLineRunner {

	private final CourseRepository courseRepository;
	private final TeacherRepository teacherRepository;
	public static void main(String[] args) {
		SpringApplication.run(ManyToOneApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception{

		//Extract Course By Teacher
		Teacher existingTeacher = teacherRepository.findById("99b9a2c9-2d6d-4cf1-8817-2a6d26df9469").orElseThrow();
		System.out.println("See the Teacher details");
		System.out.println(existingTeacher.getTeacherName());
		System.out.println(existingTeacher.getTeacherEmail());

		List<Course> courses = existingTeacher.getCourses();
		System.out.println("List of course: ");
		for(Course course: courses){
			System.out.println(course.getCourseName());
		}

	}

	public void uniDirectional(){
		Course course1 = Course.builder().courseName("C").build();
		Course course2 = Course.builder().courseName("C++").build();
		Course course3 = Course.builder().courseName("Java").build();
		Course course4 = Course.builder().courseName("Javascript").build();
		Course course5 = Course.builder().courseName("PHP").build();
		Course course6 = Course.builder().courseName(".Net").build();

		Teacher teacher1 = Teacher.builder()
							.teacherName("Amit")
							.teacherEmail("a@gmail.com")
							.build();

	    Teacher teacher2 = Teacher.builder()
							.teacherName("Binod")
							.teacherEmail("b@gmail.com")
							.build();	
							
	    Teacher teacher3 = Teacher.builder()
							.teacherName("Chetan")
							.teacherEmail("c@gmail.com")
							.build();						


		course1.setTeacher(teacher1);
		course2.setTeacher(teacher1);

		course3.setTeacher(teacher2);
		course4.setTeacher(teacher2);

		course5.setTeacher(teacher3);
		course6.setTeacher(teacher3);

		//courseRepository.saveAll(List.of(course1, course2, course3, course4, course5, course6));




		Course existingCourse = courseRepository.findById(1).orElseThrow();
		existingCourse.setCourseName("C1");
		existingCourse.getTeacher().setTeacherName("Amrit");
		//courseRepository.save(existingCourse);



		// Extract -> Course (many-to-one) => Eager  Through course, Teacher obj can also be extracted


		System.out.println("See the course details");
		System.out.println(existingCourse.getCourseName());

		Teacher existingCourseTeacher = existingCourse.getTeacher();
		System.out.println(existingCourseTeacher.getTeacherName()); 
		System.out.println(existingCourseTeacher.getTeacherEmail()); 

	}

}
