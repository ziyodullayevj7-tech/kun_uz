package jahongir.kun_uz.entity.sms;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "sms_token")
public class SmsTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "token", columnDefinition = "text")
    private String token;

    @Column(name = "created_date_and_time")
    private LocalDateTime createdDateAndTime;
}
