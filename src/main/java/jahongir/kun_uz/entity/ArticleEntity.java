package jahongir.kun_uz.entity;

import jahongir.kun_uz.enums.ArticleStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "article")
@Getter
@Setter
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "content")
    private String content;

    @Column(name = "shared_count")
    private Integer sharedCount;

    @Column(name = "image_id")
    private Integer imageId;

    @Column(name = "region_id")
    private Integer regionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", insertable = false, updatable = false)
    private RegionEntity region;

    @Column(name = "moderator_id")
    private Integer moderatorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id", insertable = false, updatable = false)
    private ProfileEntity moderator;

    @Column(name = "publisher_id")
    private Integer publisherId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publisher_id", insertable = false, updatable = false)
    private ProfileEntity publisher;

    @Column(name = "read_time")
    private String readTime;

    @CreationTimestamp
    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "published_date")
    private String publishedDate;

    @Column(name = "visible")
    private Boolean visible = true;

    @Column(name = "view_count")
    private Integer viewCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ArticleStatus status;

    @ManyToMany()
    @JoinTable(name = "article_category",
    joinColumns = @JoinColumn(name = "article_id"),
    inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<CategoryEntity> categories;

    @ManyToMany()
    @JoinTable(name = "article_section",
    joinColumns = @JoinColumn(name = "article_id"),
    inverseJoinColumns = @JoinColumn(name = "section_Id"))
    private List<SectionEntity> sections;
}
