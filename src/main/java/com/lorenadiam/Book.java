package com.lorenadiam;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity(name = "Book")
@Table(name = "book")
public class Book {

    @Id
    @SequenceGenerator(
            name = "book_sequence",
            sequenceName = "book_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "book_sequence")
    @Column(
            name = "id",
            updatable = false)
    private Long id;

    @Column(
            name = "book_name",
            nullable = false,
            columnDefinition = "TEXT")
    private String bookName;

    @Column(
            name = "created_at",
            updatable = false,
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private LocalDateTime createdAt;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(
            name = "student_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "student_book_fk"))
    private Student student; // **this student

    public Book(String bookName, LocalDateTime createdAt, Student student) { // This constructor don't require sync when adding book!
        this.bookName = bookName;
        this.createdAt = createdAt;
        this.student = student;
    }

    public Book(String bookName, LocalDateTime createdAt) {
        this.bookName = bookName;
        this.createdAt = createdAt;
    }
}
