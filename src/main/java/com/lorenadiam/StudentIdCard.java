package com.lorenadiam;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(
        name = "StudentIdCard")
@Table(
        name = "student_id_card",
        uniqueConstraints = { // Unique card_number constraint
                @UniqueConstraint(name = "student_id_card_number_unique", columnNames = "card_number")})
public class StudentIdCard {

    @Id
    @SequenceGenerator( // Id is backed by SEQUENCE which is the same as BIG SERIAL data type!
            name = "student_id_card_sequence",
            sequenceName = "student_id_card_sequence",
            allocationSize = 1 // how much will sequence increase. (default 50)
    )
    @GeneratedValue( // We have to specify this. This means like now we want this to be generated based of the sequence
            strategy = SEQUENCE,
            generator = "student_id_card_sequence" // this is defined above in sequence
    )
    @Column(
            name = "id",
            updatable = false // we don't want anyone to be able to update this
    )
    private Long id;

    @Column(name = "card_number", unique = true, length = 15)
    private String cardNumber;

    // Configuring Foreign KEY to Student class
    @OneToOne(cascade = CascadeType.ALL) //???????
    @JoinColumn(
            name = "student_id", // name of this Foreign KEY column inside student_id_card table
            referencedColumnName = "id") // this is "id" from Student class
    // This is an actual Foreign KEY property! Not classic Long or Int property as we would expect.
    // It is a Student class field since it connects to that class.
    private Student student;

    public StudentIdCard(String cardNumber, Student student) {
        this.cardNumber = cardNumber;
        this.student = student;
    }
}
