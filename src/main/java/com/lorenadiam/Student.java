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

    @OneToOne( // this means when we load the student we also load the card and vice versa. Adding JOINS.
            mappedBy = "student", // this forms BI-Directional relationship where this "student" is the field found inside StudentIdCard class.
            orphanRemoval = true, // Default: false. When deleting entity with relationship this needs to be set to "true", otherwise it will NOT delete.
            // When "true" it will delete both owner "Student" and child "StudentIdCard" entities. This should never be set on a child Entity!!!
            // ***IT DOESN'T MAKE SENSE TO HAVE orphanRemoval set to TRUE on a child (idCard) entity! When deleting "StudentIdCard" "Student" should not be deleted.
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE} // needed to not get error: save the instance before flushing
    )
    private StudentIdCard studentIdCard;

    @OneToMany( // this is Bi-directional relationship
            mappedBy = "student", // **student from Book class
            orphanRemoval = true, // PERSIST. Because of this we don't need repo for Book it can be saved
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, // when we delete, we delete all children
            fetch = FetchType.LAZY // Default for 12Many: LAZY. When we change it to "EAGER" books will be loaded together with Student!
            // Good to start with default, and check later. Think about performance if there are many books to load!
            // If application needs extra books we can make a QUERY for it and call it when we need it instead of loading everything at ones!
    )
    private List<Book> books = new ArrayList<>(); // because this is one to many we need a list in student class!

    public Student(String firstName, String lastName, String email, Integer age) { // removed "id" since it is generated automatically!
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.age = age;
    }

    public void addBook(Book book) {
        if (!this.books.contains(book)) { // if it doesn't contain book
            this.books.add(book);
            // bidirectional, both ways in sync!
            book.setStudent(this); // In "Student" we add a Book, and in other side we set the "Student" back
        }
    }

    public void removeBook(Book book) {
        if (this.books.contains(book)) {
            this.books.remove(book);
            book.setStudent(null);
        }
    }

    public void setStudentIdCard(StudentIdCard studentIdCard) {
        this.studentIdCard = studentIdCard;
    }

}
