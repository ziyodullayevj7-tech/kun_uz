package jahongir.kun_uz.service;

import jahongir.kun_uz.entity.GradeEntity;
import jahongir.kun_uz.entity.CourseEntity;
import jahongir.kun_uz.entity.StudentEntity;
import jahongir.kun_uz.repository.GradeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GradeService {

    private final GradeRepository gradeRepository;
    private final StudentService studentService;
    private final CourseService courseService;

    // ── CRUD / Scoring ────────────────────────────────────────────────────────

    public List<GradeEntity> findAll() {
        return gradeRepository.findAll();
    }

    public GradeEntity findById(String id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Grade not found with ID: " + id));
    }

    public List<GradeEntity> findByStudentId(String studentId) {
        studentService.findById(studentId); // 404 guard
        return gradeRepository.findByStudentId(studentId);
    }

    public List<GradeEntity> findByCourseId(String courseId) {
        courseService.findById(courseId); // 404 guard
        return gradeRepository.findByCourseId(courseId);
    }

    /**
     * Creates or updates a grade for a student + course pair.
     * If a grade already exists, its score is updated.
     */
    @Transactional
    public GradeEntity upsert(String studentId, String courseId, Double score) {
        StudentEntity student = studentService.findById(studentId);
        CourseEntity course = courseService.findById(courseId);

        GradeEntity grade = gradeRepository
                .findByStudentIdAndCourseId(studentId, courseId)
                .orElseGet(GradeEntity::new);

        grade.setStudent(student);
        grade.setCourse(course);
        grade.setScore(score);
        return gradeRepository.save(grade);
    }

    @Transactional
    public void delete(String id) {
        if (!gradeRepository.existsById(id)) {
            throw new EntityNotFoundException("Grade not found with ID: " + id);
        }
        gradeRepository.deleteById(id);
    }

    // ── Analytics ─────────────────────────────────────────────────────────────

    /**
     * Returns a performance summary for a student containing:
     * <ul>
     *   <li>{@code gpa}        – average score across all courses</li>
     *   <li>{@code highest}    – best single-course score</li>
     *   <li>{@code lowest}     – worst single-course score</li>
     *   <li>{@code gradeCount} – number of graded courses</li>
     * </ul>
     *
     * @param studentId the student's UUID string
     */
    public Map<String, Object> getStudentPerformanceMetrics(String studentId) {
        studentService.findById(studentId); // 404 guard
        return Map.of(
                "gpa",        gradeRepository.computeGpaByStudentId(studentId),
                "highest",    gradeRepository.computeMaxScoreByStudentId(studentId),
                "lowest",     gradeRepository.computeMinScoreByStudentId(studentId),
                "gradeCount", gradeRepository.countByStudentId(studentId)
        );
    }
}
