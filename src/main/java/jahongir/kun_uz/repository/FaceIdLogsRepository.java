package jahongir.kun_uz.repository;

import jahongir.kun_uz.dto.AnalyticsDTO;
import jahongir.kun_uz.entity.FaceIdLogEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FaceIdLogsRepository extends JpaRepository<FaceIdLogEntity, String> {

    @Override
    @EntityGraph(attributePaths = "student")
    List<FaceIdLogEntity> findAll();

    // ── Count helpers ─────────────────────────────────────────────────────────

    long countByStudentId(String studentId);

    long countByStudentIdAndDirection(String studentId, String direction);

    @Query("""
        SELECT COUNT(f)
        FROM FaceIdLogEntity f
        WHERE f.student.id = :studentId
          AND f.direction = :direction
          AND f.timestamp >= :since
    """)
    long countByStudentIdAndDirectionSince(
            @Param("studentId") String studentId,
            @Param("direction") String direction,
            @Param("since") LocalDateTime since);

    // ── Analytics ─────────────────────────────────────────────────────────────

    /**
     * Attendance leaderboard: students ranked by campus-entry count within the
     * last {@code pastDays} days.
     */
    @Query("""
        SELECT new jahongir.kun_uz.dto.AnalyticsDTO$FaceIdFrequencyMetric(
            s.id,
            CONCAT(s.firstName, ' ', s.lastName),
            COUNT(f.id),
            MAX(f.timestamp)
        )
        FROM FaceIdLogEntity f
        JOIN f.student s
        WHERE f.direction = 'IN'
          AND f.timestamp >= :since
        GROUP BY s.id, s.firstName, s.lastName
        ORDER BY COUNT(f.id) DESC
    """)
    List<AnalyticsDTO.FaceIdFrequencyMetric> fetchAttendanceLeaderboard(@Param("since") LocalDateTime since);

    /**
     * Per-student access ratio: total IN-entries vs total OUT-entries.
     * Returned as a two-element Object[] per student — post-processed in the service.
     */
    @Query("""
        SELECT
            f.direction,
            COUNT(f.id)
        FROM FaceIdLogEntity f
        WHERE f.student.id = :studentId
        GROUP BY f.direction
    """)
    List<Object[]> fetchDirectionCountsForStudent(@Param("studentId") String studentId);
}
