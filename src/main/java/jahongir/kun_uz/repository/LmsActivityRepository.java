package jahongir.kun_uz.repository;

import jahongir.kun_uz.dto.AnalyticsDTO;
import jahongir.kun_uz.entity.LmsActivityEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LmsActivityRepository extends JpaRepository<LmsActivityEntity, String> {

    @Override
    @EntityGraph(attributePaths = "student")
    List<LmsActivityEntity> findAll();

    // ── Sum / count helpers ───────────────────────────────────────────────────

    @Query("""
        SELECT COALESCE(SUM(l.durationMinutes), 0)
        FROM LmsActivityEntity l
        WHERE l.student.id = :studentId
    """)
    Long sumDurationMinutesByStudentId(@Param("studentId") String studentId);

    @Query("""
        SELECT COALESCE(SUM(l.durationMinutes), 0)
        FROM LmsActivityEntity l
        WHERE l.student.id = :studentId
          AND l.createdAt >= :since
    """)
    Long sumDurationMinutesByStudentIdSince(
            @Param("studentId") String studentId,
            @Param("since") LocalDateTime since);

    long countByStudentId(String studentId);

    // ── Analytics ─────────────────────────────────────────────────────────────

    /**
     * LMS engagement leaderboard: students ranked by active minutes in the last
     * {@code pastDays} days.
     */
    @Query("""
        SELECT new jahongir.kun_uz.dto.AnalyticsDTO$LmsEngagementMetric(
            s.id,
            CONCAT(s.firstName, ' ', s.lastName),
            COALESCE(SUM(l.durationMinutes), 0L),
            COUNT(l.id)
        )
        FROM LmsActivityEntity l
        JOIN l.student s
        WHERE l.createdAt >= :since
        GROUP BY s.id, s.firstName, s.lastName
        ORDER BY COALESCE(SUM(l.durationMinutes), 0) DESC
    """)
    List<AnalyticsDTO.LmsEngagementMetric> fetchEngagementLeaderboard(@Param("since") LocalDateTime since);

    /**
     * Per-student study hours summary for all time.
     */
    @Query("""
        SELECT new jahongir.kun_uz.dto.AnalyticsDTO$LmsEngagementMetric(
            s.id,
            CONCAT(s.firstName, ' ', s.lastName),
            COALESCE(SUM(l.durationMinutes), 0L),
            COUNT(l.id)
        )
        FROM LmsActivityEntity l
        JOIN l.student s
        WHERE s.id = :studentId
        GROUP BY s.id, s.firstName, s.lastName
    """)
    java.util.Optional<AnalyticsDTO.LmsEngagementMetric> fetchStudentStudyHours(@Param("studentId") String studentId);
}
