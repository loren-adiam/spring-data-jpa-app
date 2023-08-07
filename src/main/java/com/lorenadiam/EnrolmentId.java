package com.lorenadiam;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode // for this class to be composite key we will need getters, setters, NoArgsCons, Equals & HashCode!
@Embeddable // Class will be embeddable to another entity/class. @Embeddable requires "Serializable" interface every time!
public class EnrolmentId implements Serializable { // This class is only for COMPOSITE keys. It captures these 2 ids.

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "course_id")
    private Long courseId;

    public EnrolmentId(Long studentId, Long courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
    }

}
