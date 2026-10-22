package jahongir.kun_uz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
public class StudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 20)
    private String studentNumber;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String status = "ACTIVE";
}