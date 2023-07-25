package com.lorenadiam;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.List;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(StudentRepository studentRepository) {
		return args -> {
			Student maida = new Student("Maida", "Karic", "maida-karic@gmail.com",32);
			Student nerol = new Student("Nerol", "Karic", "nerol-karic@gmail.com",30);
			//studentRepository.save(maida);

			System.out.println("\nAdding Maida and Nerol");
			studentRepository.saveAll(List.of(maida, nerol));

			System.out.println("\nStudents count: " + studentRepository.count());


			studentRepository
					.findById(2L)
					.ifPresentOrElse(
							System.out::println, // if found (prints student with that id, toString())
							() -> System.out.println("\nStudent with Id 2 not found.")); // if not found

			studentRepository
					.findById(3L)
					.ifPresentOrElse(
							System.out::println,
							() -> System.out.println("Student with Id 1 not found."));

			System.out.println("\nSelect all students.");
			List<Student> students = studentRepository.findAll();
			students.forEach(System.out::println);

			System.out.println("\nDelete student with id: 1");
			studentRepository.deleteById(1L);

			System.out.println("\nNew count: " + studentRepository.count());

		};
	}
}
