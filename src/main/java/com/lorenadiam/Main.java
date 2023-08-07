package com.lorenadiam;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Main {

	private static final Faker faker = new Faker();

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(
			StudentRepository studentRepository) {

		return args -> {

			Student student1 = generateStudent(studentRepository); // not saving only generating

			StudentIdCard studentIdCard = new StudentIdCard("123456789", student1);

			// adding books through Student constructor which will be saved later too. addBook() is syncing the Student class too!
			student1.addBook(new Book("Clean code", LocalDateTime.now().minusDays(4)));
			student1.addBook(new Book("Think and Grow Rich", LocalDateTime.now()));
			student1.addBook(new Book("Spring Data JPA", LocalDateTime.now().minusYears(1)));

			// setting the id card on student1 to be able to save it (card) to db on the next step together with the student!
			student1.setStudentIdCard(studentIdCard); // only setting studentIdCard, and later to be able to save it

//			student1.enrolToCourse(new Course("Computer Science", "IT"));
//			student1.enrolToCourse(new Course("Spring Data JPA", "IT"));

			studentRepository.save(student1); // saving student, also studentIdCard and now Books too!
			// We are basically creating & saving all the data through the student object. Beside the actual student, we also set card and books.

			studentRepository.findById(1L) // testing BIDirectional relationship. It will add JOIN to student id card!
					.ifPresent(s -> { // Student Card is loaded automatically since FetchType is EAGER for 121.
						System.out.println("Fetch book lazy..."); // Books not loaded since FetchType is LAZY for 12M/M21!
						List<Book> books = student1.getBooks();// This is how we force it to load the Books too from db!
						books.forEach(book -> { // or we put FetchType to EAGER.
							System.out.println(s.getFirstName() + " borrowed " + book.getBookName());
						});
					});

//			studentIdCardRepository.findById(1L) // this is to see in logs how hibernate did left join
//					.ifPresent(System.out::println); // toString() showing both Student and StudentIdCard data!
//			studentRepository.deleteById(1L); // Testing Orphan removal. It will delete both Student and StudentIdCard entities

//			generateAndSaveRandomStudents(studentRepository);
			// Paging examples. Return 5 students per page example
//			pagingExamples(studentRepository);

			// sorting on First Name
//			sortingExamples(studentRepository);

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

	private static void pagingExamples(StudentRepository studentRepository) {
		PageRequest pageRequest = PageRequest.of(
				0,
				5, //  means 1st page, 5 is the limit the request to 5 students. (5 per page)
				Sort.by("firstName").ascending()); // It is possible to add sorting too.
		Page<Student> studentPage = studentRepository.findAll(pageRequest);
		System.out.println(studentPage); // we can set break point here and debug to check the pages and so on...
	}

	private static void sortingExamples(StudentRepository studentRepository) {
		Sort firstSort = Sort.by(Sort.Direction.ASC, "firstName");
		studentRepository.findAll(firstSort) // findAll() takes Sort objects
				.forEach(student -> System.out.println(student.getFirstName()));

		// sorting on First Name and Age (difference approach)
		Sort secondSort = Sort.by("firstName").ascending().and(Sort.by("age")).descending();
		studentRepository.findAll(secondSort).forEach(
				student -> System.out.println(student.getFirstName() + ", " + student.getAge())
		);
	}

	private static void generateAndSaveRandomStudents(StudentRepository studentRepository) {
		for (int i = 0; i < 20; i++) {
			Student student = generateStudent(studentRepository);
			studentRepository.save(student);
		}
	}

	private static Student generateStudent(StudentRepository studentRepository) {
		String firstName = faker.name().firstName();
		String lastName = faker.name().lastName();
		String email = String.format("%s.%s@gmail.com", firstName.toLowerCase(), lastName.toLowerCase());
		int age = faker.number().numberBetween(17, 55);

		Student student = new Student(firstName, lastName, email, age);
//		studentRepository.save(student);

		return student;
	}


}
