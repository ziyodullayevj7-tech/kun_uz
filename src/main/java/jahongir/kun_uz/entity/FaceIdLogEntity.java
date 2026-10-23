package jahongir.kun_uz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "face_id_logs")
@Getter
@Setter
@NoArgsConstructor
public class FaceIdLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @Column(nullable = false, length = 100)
    private String location; // e.g. "Main Gate", "Library", "Building B"

    @Column(nullable = false, length = 10)
    private String direction; // "IN" or "OUT"

    @Column(nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();
}
