package com.lorenadiam;

import com.github.javafaker.Faker;
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

			Faker faker = new Faker();
			for (int i = 0; i < 20; i++) {
				String firstName = faker.name().firstName();
				String lastName = faker.name().lastName();
				String email = String.format("%s.%s@gmail.com", firstName.toLowerCase(), lastName.toLowerCase());
				int age = faker.number().numberBetween(17, 55);

				Student student = new Student(firstName, lastName, email, age);
				studentRepository.save(student);

			}


			// Fake Data
			/*Student maida = new Student("Maida", "Karic", "maida-karic@gmail.com",32);
			Student nerol = new Student("Nerol", "Karic", "nerol-karic@gmail.com",30);
			Student maida2 = new Student("Maida", "Topcagic", "maida-topcagic@gmail.com",25);
			//studentRepository.save(maida);
			System.out.println("\nAdding Maida and Nerol");
			studentRepository.saveAll(List.of(maida, nerol, maida2));*/

			// First code
			/*System.out.println("\nStudents count: " + studentRepository.count());

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

			System.out.println("\nNew count: " + studentRepository.count());*/

			// Second code
			/*System.out.println("\nFirst JPQL query");
			studentRepository
					.findStudentByEmail("nerol-karic@gmail.com")
					.ifPresentOrElse(
							System.out::println,
							() -> System.out.println("Student with this email not found!"));

			System.out.println("\nSecond JPQL query");
			studentRepository
					.findStudentsByFirstNameEqualsAndAgeGreaterThan("Maida", 22)
					.forEach(System.out::println);

			System.out.println("\nThird Native query");
			studentRepository
					.findStudentsByFirstNameEqualsAndAgeGreaterThanNative("Maida", 22)
					.forEach(System.out::println);

			System.out.println("\nThird Native query with different param setup");
			studentRepository
					.testDifferentParamLogic("Maida", 24)
					.forEach(System.out::println);

			System.out.println("\nFourth JPQL Delete query");
			int countDeletedStudents = studentRepository
					.deleteStudentById(3L);
			System.out.println("Deleted Students Count: " + countDeletedStudents);

			System.out.println("\nFifth JPQL Update query");
			int rowsAffected = studentRepository
					.updateStudentById("Maja", 2L);
			System.out.println("Rows affected: " + rowsAffected);*/
		};
	}




}
