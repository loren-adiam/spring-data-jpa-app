package com.lorenadiam;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    // This is how we can create custom SQL queries with help of these "methods" (chaining) from Spring Data JPA. findStudentBy + Email
    // We can use annotation @Query to allow us to write "JPQL" (java persistence query language) queries and "NATIVE" queries.
    // We take FULL control of the query (we can also override it). It is best practice to have this always on top of methods!
    @Query("SELECT s FROM Student s WHERE s.email = ?1") // FROM -> Student (from @Entity(name = "Student")
    Optional<Student> findStudentByEmail(String email); // from this method Spring Data JPA will generate SQL query! Nice!

    // When we write this @Query we take full control and override method below. We can even change name of function if we want since @Query applies anyway.
    @Query("SELECT s FROM Student s WHERE s.firstName = ?1 AND s.age >= ?2") // ?1 and ?2 represents 1st & 2nd arguments in the method
    List<Student> findStudentsByFirstNameEqualsAndAgeGreaterThan(String firstName, Integer age);

}
