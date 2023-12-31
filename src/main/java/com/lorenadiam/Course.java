package com.lorenadiam;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "Course")
@Table(name = "course")
public class Course {

    @Id
    @SequenceGenerator(
            name = "course_sequence",
            sequenceName = "course_sequence",
            allocationSize = 1)
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "course_sequence")
    @Column(
            name = "id",
            updatable = false)
    private Long id;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "department",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String department;

    public Course(String name, String department) {
        this.name = name;
        this.department = department;
    }


    // Old setup
    /*@ManyToMany(mappedBy = "courses")
    private List<Student> students = new ArrayList<>();*/

    // New setup. Same as in Student class. COPY, PASTE
    @OneToMany(mappedBy = "course", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Enrolment> enrolments = new ArrayList<>();

    public void addEnrolment(Enrolment enrolment){
        if (!enrolments.contains(enrolment)) {
            enrolments.add(enrolment);
        }
    }
    public void removeEnrolment(Enrolment enrolment){
        enrolments.remove(enrolment);
    }
}
