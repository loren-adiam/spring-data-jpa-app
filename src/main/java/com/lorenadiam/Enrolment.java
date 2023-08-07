package com.lorenadiam;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity(name = "Enrolment")
@Table(name = "enrolment")
public class Enrolment {

    @EmbeddedId
    private EnrolmentId id; // this is our composite key!

    @ManyToOne
    @MapsId("studentId") // this means just that this student is part of the id (studentId from enrolmentID class)
    @JoinColumn(name = "student_id", foreignKey = @ForeignKey(name = "enrolment_student_id_fk")) // property in db
    private Student student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id", foreignKey = @ForeignKey(name = "enrolment_course_id_fk"))
    private Course course;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private LocalDateTime createdAt;

    public Enrolment(EnrolmentId id, Student student, Course course, LocalDateTime createdAt) { // We need id since it is not auto generated.
        this.id = id;
        this.student = student;
        this.course = course;
        this.createdAt = createdAt;
    }

    public Enrolment(Student student, Course course, LocalDateTime createdAt) {
        this.student = student;
        this.course = course;
        this.createdAt = createdAt;
    }
}
