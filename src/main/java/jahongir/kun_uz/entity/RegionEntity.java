package jahongir.kun_uz.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "region")
@Getter
@Setter
public class RegionEntity {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "order_number")
    private String orderNumber;

    @Column(name = "name_uz")
    private String nameUz;

    @Column(name = "name_ru")
    private String nameRu;

    @Column(name = "name_en")
    private String nameEn;

    @Column
    private String key;

    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDate createdDate;
}
