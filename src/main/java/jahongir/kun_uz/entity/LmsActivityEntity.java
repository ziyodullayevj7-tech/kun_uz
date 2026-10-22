package jahongir.kun_uz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "lms_activities")
@Getter
@Setter
@NoArgsConstructor
public class LmsActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @Column(nullable = false, length = 50)
    private String action; // e.g. "LOGIN", "WATCH_VIDEO", "SUBMIT_HOMEWORK"

    private Integer durationMinutes; // Minutes spent online

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
