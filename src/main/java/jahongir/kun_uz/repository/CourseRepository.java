package jahongir.kun_uz.repository;

import jahongir.kun_uz.dto.AnalyticsDTO;
import jahongir.kun_uz.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, String> {

    boolean existsByCode(String code);

    Optional<CourseEntity> findByCode(String code);

    // ── Analytics ────────────────────────────────────────────────────────────

    /**
     * Returns grade statistics for every course that has at least one grade.
     */
    @Query("""
        SELECT new jahongir.kun_uz.dto.AnalyticsDTO$CourseGradeAnalytics(
            c.id,
            c.name,
            AVG(g.score),
            MAX(g.score),
            MIN(g.score),
            COUNT(DISTINCT g.student.id)
        )
        FROM CourseEntity c
        JOIN GradeEntity g ON g.course = c
        GROUP BY c.id, c.name
        ORDER BY AVG(g.score) DESC
    """)
    List<AnalyticsDTO.CourseGradeAnalytics> fetchAllCourseGradeAnalytics();

    /**
     * Returns grade statistics for a single course.
     */
    @Query("""
        SELECT new jahongir.kun_uz.dto.AnalyticsDTO$CourseGradeAnalytics(
            c.id,
            c.name,
            AVG(g.score),
            MAX(g.score),
            MIN(g.score),
            COUNT(DISTINCT g.student.id)
        )
        FROM CourseEntity c
        JOIN GradeEntity g ON g.course = c
        WHERE c.id = :courseId
        GROUP BY c.id, c.name
    """)
    Optional<AnalyticsDTO.CourseGradeAnalytics> fetchCourseGradeAnalytics(@Param("courseId") String courseId);
}
