package com.tastytown.backend;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tastytown.backend.constants.Role;
import com.tastytown.backend.entity.User;
import com.tastytown.backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@SpringBootApplication
@RequiredArgsConstructor
public class BackendApplication {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${upload.file.dir}") // Spring Expression Language
	private String FILE_DIR;


	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(){
		return args -> {
			var file = new File(FILE_DIR);
			if(!file.exists()){
				file.mkdir();
				System.out.println("Folder created to store food images");
			}

			System.out.println("Folder already exists");


			// default admin creation
			String defaultAdminEmail = "admin@gmail.com";
			String defaultAdminPassword = "admin";
			var existingAdmin = userRepository.findByUserEmail(defaultAdminEmail);

			if(!existingAdmin.isPresent()){
				User adminUser = User.builder()
								.userEmail(defaultAdminEmail)
								.userPassword(passwordEncoder.encode(defaultAdminPassword))
								.role(Role.ROLE_ADMIN)
								.build();

				userRepository.save(adminUser);
				System.out.println("Default admin created: "+ defaultAdminEmail + " / " +defaultAdminPassword);				
			}else{
				System.out.println("Default admin already exists: "+ defaultAdminEmail + " / " +defaultAdminPassword);
			}
		};
	}

}
