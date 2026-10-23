package jahongir.kun_uz.repository;

import jahongir.kun_uz.entity.GradeEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<GradeEntity, String> {

    @Override
    @EntityGraph(attributePaths = {"student", "course"})
    List<GradeEntity> findAll();

    List<GradeEntity> findByStudentId(String studentId);

    List<GradeEntity> findByCourseId(String courseId);

    Optional<GradeEntity> findByStudentIdAndCourseId(String studentId, String courseId);

    boolean existsByStudentIdAndCourseId(String studentId, String courseId);

    // ── Aggregate helpers ─────────────────────────────────────────────────────

    @Query("""
        SELECT COALESCE(AVG(g.score), 0.0)
        FROM GradeEntity g
        WHERE g.student.id = :studentId
    """)
    Double computeGpaByStudentId(@Param("studentId") String studentId);

    @Query("""
        SELECT COALESCE(MAX(g.score), 0.0)
        FROM GradeEntity g
        WHERE g.student.id = :studentId
    """)
    Double computeMaxScoreByStudentId(@Param("studentId") String studentId);

    @Query("""
        SELECT COALESCE(MIN(g.score), 0.0)
        FROM GradeEntity g
        WHERE g.student.id = :studentId
    """)
    Double computeMinScoreByStudentId(@Param("studentId") String studentId);

    long countByStudentId(String studentId);
}
