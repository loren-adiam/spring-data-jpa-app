package com.lorenadiam;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// interface doesn't need annotation, but if we would have multiple class implementations they would need @Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // This is how we can create custom SQL queries with help of these "methods" (chaining) from Spring Data JPA. findStudentBy + Email
    // We can use annotation @Query to allow us to write "JPQL" (java persistence query language) queries and "NATIVE" queries.
    // We take FULL control of the query (we can also override it). It is best practice to have this always on top of methods!
    @Query("SELECT s FROM Student s WHERE s.email = ?1") // FROM -> Student (from @Entity(name = "Student")
    Optional<Student> findStudentByEmail(String email); // from this method Spring Data JPA will generate SQL query! Nice!

    // When we write this @Query we take full control and override method below. We can even change name of function if we want since @Query applies anyway.
    @Query("SELECT s FROM Student s WHERE s.firstName = ?1 AND s.age >= ?2") // THESE QUERIES ARE JPQL, NOT SQL. ?1 and ?2 represents 1st & 2nd arguments in the method
    List<Student> findStudentsByFirstNameEqualsAndAgeGreaterThan(String firstName, Integer age);

    // Now we write NATIVE postgres queries (Native and JPQL syntax is similar, but not the same)
    // "SELECT s FROM Student s WHERE s.firstName = ?1 AND s.age >= ?2" JPQL
    // "SELECT * FROM student WHERE first_name = ?1 and age >= ?2" NATIVE
    // **IMPORTANT** JPQL is better since it is not database specific! If we would e.g. to change our database
    // from postgresql to mysql our native queries might not work anymore, but JPQL queries will always work.
    // We should use Native only if there are very specific queries which don't work with JPQL!
    @Query(value = "SELECT * FROM student WHERE first_name = ?1 and age >= ?2", nativeQuery = true)
    List<Student> findStudentsByFirstNameEqualsAndAgeGreaterThanNative(String firstName, Integer age);

    @Query(value = "SELECT * FROM student WHERE first_name = :firstName and age >= :age", nativeQuery = true)
    List<Student> testDifferentParamLogic( // Not by order/position anymore. We need to add @Param annotations
            @Param("firstName") String firstName, @Param("age") Integer age);

    // Now Deleting or Updating. As I understood these 2 queries can only return integer (rows affected) or void!
    @Transactional // These queries need to be inside a TRANSACTION and that's what this annotation is for!
    @Modifying // This tells Spring Data that query doesn't need to map anything from database int Entities like before.
    @Query("DELETE FROM Student u WHERE u.id = ?1")
    int deleteStudentById(Long id);

    @Transactional // This annotation is needed for DELETING AND UPDATING, not for getting the data.
    @Modifying // This annotation is also needed only for DELETING AND UPDATING!
    @Query("UPDATE Student s SET s.firstName = :firstName WHERE s.id = :id")
    int updateStudentById(
            @Param("firstName") String firstName,
            @Param("id") Long id);


}
