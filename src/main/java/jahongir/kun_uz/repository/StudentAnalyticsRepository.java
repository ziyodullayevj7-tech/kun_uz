package jahongir.kun_uz.repository;

import jahongir.kun_uz.dto.AnalyticsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudentAnalyticsRepository {

    private final JdbcTemplate jdbcTemplate;

    public void rebuildAnalyticsTable() {
        jdbcTemplate.execute("""
            CREATE TABLE IF NOT EXISTS student_analytics (
                analytics_rank BIGINT NOT NULL,
                student_id VARCHAR(36) PRIMARY KEY,
                full_name VARCHAR(101) NOT NULL,
                student_number VARCHAR(20) NOT NULL,
                gpa_score DOUBLE PRECISION NOT NULL,
                total_campus_visits BIGINT NOT NULL,
                total_lms_minutes BIGINT NOT NULL,
                generated_at TIMESTAMP NOT NULL
            )
            """);
        jdbcTemplate.update("DELETE FROM student_analytics");
        jdbcTemplate.update("""
            INSERT INTO student_analytics (
                analytics_rank,
                student_id,
                full_name,
                student_number,
                gpa_score,
                total_campus_visits,
                total_lms_minutes,
                generated_at
            )
            SELECT
                ROW_NUMBER() OVER (
                    ORDER BY analytics.gpa_score DESC NULLS LAST,
                             analytics.full_name ASC,
                             analytics.student_number ASC
                ),
                analytics.student_id::text,
                analytics.full_name,
                analytics.student_number,
                analytics.gpa_score,
                analytics.total_campus_visits,
                analytics.total_lms_minutes,
                CURRENT_TIMESTAMP
            FROM (
                SELECT
                    s.id AS student_id,
                    CONCAT(s.first_name, ' ', s.last_name) AS full_name,
                    s.student_number,
                    (
                        SELECT AVG(g.score)
                        FROM grades g
                        WHERE g.student_id = s.id
                    ) AS gpa_score,
                    (
                        SELECT COUNT(f.id)
                        FROM face_id_logs f
                        WHERE f.student_id = s.id AND f.direction = 'IN'
                    ) AS total_campus_visits,
                    COALESCE((
                        SELECT SUM(l.duration_minutes)
                        FROM lms_activities l
                        WHERE l.student_id = s.id
                    ), 0) AS total_lms_minutes
                FROM students s
                WHERE s.status = 'ACTIVE'
                  AND EXISTS (
                      SELECT 1
                      FROM grades g
                      WHERE g.student_id = s.id
                  )
            ) analytics
            ORDER BY analytics.gpa_score DESC NULLS LAST,
                     analytics.full_name ASC,
                     analytics.student_number ASC
            """);
    }

    public List<AnalyticsDTO.StudentSummaryAnalytics> findAll() {
        return jdbcTemplate.query("""
            SELECT student_id,
                   full_name,
                   student_number,
                   gpa_score,
                   total_campus_visits,
                   total_lms_minutes
            FROM student_analytics
            ORDER BY analytics_rank
            """, (rs, rowNum) -> new AnalyticsDTO.StudentSummaryAnalytics(
                rs.getString("student_id"),
                rs.getString("full_name"),
                rs.getString("student_number"),
                rs.getObject("gpa_score", Double.class),
                rs.getLong("total_campus_visits"),
                rs.getLong("total_lms_minutes")
            ));
    }
}
