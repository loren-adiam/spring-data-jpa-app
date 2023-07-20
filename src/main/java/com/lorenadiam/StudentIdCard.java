package com.lorenadiam;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Data // includes toString, EqualsAndHashCode, Getter, Setter (non-final fields), RequiredArgsConstructor!
@NoArgsConstructor
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
    @OneToOne(
            cascade = CascadeType.ALL, // ALL/PERSIST is allowing us to save the child entity "Student" when we save the card.
            fetch = FetchType.EAGER) // Default FetchType for "121" is EAGER (when fetching data (findById) it loads both owner and child), LAZY only loads owner!
    // This is ok for "121", but when we have "12Many" or "Many2Many" default is: LAZY to avoid loading too much unnecessary data
    // ALL (includes all types below!), PersistenceContext 1st level cash between app and db, Owning entity: StudentIdCard, child: Student
    // PERSIST (This is when we save entity) It saves both owning and child entity!!!. This example here.
    // MERGE (coping the entity to the PersistenceContext)
    // REMOVE (remove entity from db)
    // REFRESH (get entity again from the database)
    // DETACH (the child will also get removed from the PersistenceContext) ;
    // We should never set orphanRemoval here since it doesn't make sense to delete "Student" when deleting "StudentIdCard"!
    @JoinColumn( // we are adding this only in tables which have FK! Owner table without FK don't need this annotation!
            name = "student_id", // name of this Foreign KEY column inside student_id_card table
            referencedColumnName = "id", // this is "id" from Student class
            foreignKey = @ForeignKey(name = "student_id_fk")) // (Best practice) This is how to modify WEIRD LONG default foreignKey constraint name!

    // This is an actual Foreign KEY property! Not classic Long or Int property as we would expect.
    // It is a Student class field since it connects to that class.
    private Student student;

    public StudentIdCard(String cardNumber, Student student) { // constructor without "id"
        this.cardNumber = cardNumber;
        this.student = student;
    }
}
