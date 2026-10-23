package jahongir.kun_uz.repository;

import jahongir.kun_uz.entity.StudentEntity;
import jahongir.kun_uz.dto.AnalyticsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, String> {

    boolean existsByStudentNumber(String studentNumber);
    boolean existsByEmail(String email);

    /**
     * Cross-domain analytics for every ACTIVE student.
     *
     * Three correlated subqueries are used (one per domain entity) because
     * StudentEntity has no @OneToMany fields — JPQL LEFT JOIN … ON is only
     * valid for navigable association paths, so we project each aggregate
     * independently via a scalar subselect instead.
     *
     * COALESCE is intentionally omitted:
     *  - COUNT never returns null.
     *  - AVG / SUM may return null, but the record fields are boxed
     *    (Double, Long), so null is a valid value for students with no data.
     */
    @Query("""
        SELECT new jahongir.kun_uz.dto.AnalyticsDTO$StudentSummaryAnalytics(
            s.id,
            CONCAT(s.firstName, ' ', s.lastName),
            s.studentNumber,
            (SELECT AVG(g.score)           FROM GradeEntity       g WHERE g.student = s),
            (SELECT COUNT(f.id)            FROM FaceIdLogEntity   f WHERE f.student = s AND f.direction = 'IN'),
            (SELECT SUM(l.durationMinutes) FROM LmsActivityEntity l WHERE l.student = s)
        )
        FROM StudentEntity s
        WHERE s.status = 'ACTIVE'
        ORDER BY (SELECT AVG(g2.score) FROM GradeEntity g2 WHERE g2.student = s) DESC
    """)
    List<AnalyticsDTO.StudentSummaryAnalytics> fetchAllStudentsAnalytics();
}