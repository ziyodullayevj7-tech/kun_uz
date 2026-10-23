package jahongir.kun_uz.service;

import jahongir.kun_uz.dto.AnalyticsDTO;
import jahongir.kun_uz.entity.CourseEntity;
import jahongir.kun_uz.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepository courseRepository;

    // ── CRUD ──────────────────────────────────────────────────────────────────

    public List<CourseEntity> findAll() {
        return courseRepository.findAll();
    }

    public CourseEntity findById(String id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + id));
    }

    @Transactional
    public CourseEntity create(CourseEntity course) {
        if (courseRepository.existsByCode(course.getCode())) {
            throw new IllegalArgumentException("Course code already exists: " + course.getCode());
        }
        return courseRepository.save(course);
    }

    @Transactional
    public CourseEntity update(String id, CourseEntity incoming) {
        CourseEntity current = findById(id);
        current.setName(incoming.getName());
        current.setCredits(incoming.getCredits());
        return courseRepository.save(current);
    }

    @Transactional
    public void delete(String id) {
        if (!courseRepository.existsById(id)) {
            throw new EntityNotFoundException("Course not found with ID: " + id);
        }
        courseRepository.deleteById(id);
    }

    // ── Analytics ─────────────────────────────────────────────────────────────

    /**
     * Returns grade statistics (avg, highest, lowest, student count) for all
     * courses that have at least one graded student.
     */
    public List<AnalyticsDTO.CourseGradeAnalytics> getAllCoursePerformance() {
        return courseRepository.fetchAllCourseGradeAnalytics();
    }

    /**
     * Returns grade statistics for a single course.
     *
     * @param courseId the course's UUID string
     * @return an {@link Optional} containing the analytics or empty if the course
     *         has no grades yet
     */
    public Optional<AnalyticsDTO.CourseGradeAnalytics> getCoursePerformance(String courseId) {
        // Verify the course exists first so callers get a meaningful 404
        if (!courseRepository.existsById(courseId)) {
            throw new EntityNotFoundException("Course not found with ID: " + courseId);
        }
        return courseRepository.fetchCourseGradeAnalytics(courseId);
    }
}
