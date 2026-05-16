package jahongir.kun_uz.entity;

import jahongir.kun_uz.enums.Roles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "profile_role")
public class ProfileRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", insertable = false, updatable = false, nullable = false)
    ProfileEntity profile;

    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    private Roles role;


    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;
}
