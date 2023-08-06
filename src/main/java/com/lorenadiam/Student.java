package com.lorenadiam;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Data
@NoArgsConstructor
@Entity(name = "Student") // Default (class name). This annotation is used to map this class to table.
// Good practice to have "name" specified to have full control. E.g. long class name, but entity name different.
@Table( // similar to @Column for fields, here we want to take control over table
    name = "student",
    uniqueConstraints = { // This is to take control of the unique email constraint name if we want!
            @UniqueConstraint(name = "student_email_unique", columnNames = "email")
    }
)
public class Student {

    @Id
    @SequenceGenerator( // Id is backed by SEQUENCE which is the same as BIG SERIAL data type!
            name = "student_sequence",
            sequenceName = "student_sequence",
            allocationSize = 1 // how much will sequence increase. (default 50)
    )
    @GeneratedValue( // We have to specify this. This means like now we want this to be generated based of the sequence
            strategy = SEQUENCE,
            generator = "student_sequence" // this is defined above in sequence
    )
    @Column(
            name = "id",
            updatable = false // we don't want anyone to be able to update this
    )
    private Long id;

    @Column(
            name = "first_name", // this way we have full control of column name in database!
            nullable = false, // can't be NULL value!
            columnDefinition = "TEXT" // it was varchar(255) by default, and we changed it to TEXT
    )
    private String firstName;

    @Column(
            name = "last_name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String lastName;

    @Column(
            name = "email",
            nullable = false,
            columnDefinition = "TEXT",
            unique = true // we want email to be unique for each student!
    )
    private String email;

    @Column(name = "age", nullable = false)
    private Integer age;

    @OneToOne( // (If we need it) This gives us ability (depends on CascadeType?) when loading student we also load the student card and vice versa. Adding JOINS.
            mappedBy = "student", // this forms BI-Directional relationship where this "student" is the field found inside StudentIdCard class.
            orphanRemoval = true, // Default: false. When deleting entity with relationship (FK constrain) this needs to be set to "true", otherwise it will NOT delete.
            // When "true" it will delete both owner "Student" and child "StudentIdCard" entities. This should never be set on a child Entity!!!
            // ***IT DOESN'T MAKE SENSE TO HAVE orphanRemoval set to TRUE on a child (idCard) entity! When deleting "StudentIdCard" "Student" should not be deleted.
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE} // Added the same CascadeType logic like below from "OneToMany"
            // It was actually needed to not get error: save the instance before flushing
    )
    private StudentIdCard studentIdCard; // BI_D, the only reason we have this is to be able to save it together with student I guess. If not exists we could save card separately.

    @OneToMany( // this is Bi-directional relationship
            mappedBy = "student", // **student object from Book class
            orphanRemoval = true, // PERSIST. Because of this we don't need repo for Book it can be saved
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, // when we delete, we delete all children
            fetch = FetchType.LAZY // Default for 12Many: LAZY. When we change it to "EAGER" books will be loaded together with Student!
            // Good to start with default: LAZY, and check later. Think about performance if there are many books to load!
            // If application needs extra books we can also make a QUERY for it and call it when we need it instead of loading everything at ones!
    )
    private List<Book> books = new ArrayList<>(); // because this is one to many we need a list in student class!

    // this NEW* annotation will automatically create Enrolment table
    @JoinTable(
            name = "enrolment",
            joinColumns = @JoinColumn(
                    name = "student_id", foreignKey = @ForeignKey(name = "enrolment_student_id_fk")),
            inverseJoinColumns = @JoinColumn(
                    name = "course_id", foreignKey = @ForeignKey(name = "enrolment_course_id_fk"))
    )
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Course> courses = new ArrayList<>(); // "mapped by" in Course class

    public Student(String firstName, String lastName, String email, Integer age) { // removed "id" since it is generated automatically!
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
    }

    // because this is Bi-directional with Book we need to add few methods here. Adding Books through Student object requires sync with it.
    public void addBook(Book book) {
        if (!this.books.contains(book)) { // if books list doesn't contain book we can add it
            this.books.add(book);
            // Bidirectional, both ways needs to be in sync!
            book.setStudent(this); // In "Student" we add a Book, and in other side (Book class) we also set the "Student" back...
            // When we load the book we want to load the Students, and when we load the Student we also want to load associated Books.
            // Setter from Book class. It is kind of connecting this current Book to this current Student object.
            // Since we are not using Book constructor which has Student as param (then this not needed), we need to set this student here, otherwise
            // it doesn't know which student has this book and when trying to save student object we will get an ERROR!
        }
    }

    public void removeBook(Book book) {
        if (this.books.contains(book)) {
            this.books.remove(book);
            book.setStudent(null); // same as above when adding book
        }
    }
    // we added this to be able to set StudentIdCard from Student object, and we later do save on the Student
    public void setStudentIdCard(StudentIdCard studentIdCard) {
        this.studentIdCard = studentIdCard;
    }


}
