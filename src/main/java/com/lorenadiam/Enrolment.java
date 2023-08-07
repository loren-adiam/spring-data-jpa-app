package com.lorenadiam;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "Enrolment")
@Table(name = "enrolment")
public class Enrolment {

    @EmbeddedId
    private EnrolmentId id; // this is our composite key!

    @ManyToOne
    @MapsId("studentId") // this means just that this student is part of the id (studentId from enrolmentID class)
    @JoinColumn(name = "student_id") // property in db
    private Student student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;
}
