package jahongir.kun_uz.entity;

import jahongir.kun_uz.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "profile")
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "photo_id")
    private String photoId;

    @Column(name = "visible", nullable = false)
    private Boolean visible = true;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDate createdDate;

    @OneToMany(mappedBy = "profile", fetch = FetchType.LAZY)
    private List<ProfileRoleEntity> roleList;
}
