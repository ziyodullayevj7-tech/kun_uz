package jahongir.kun_uz.service;

import jahongir.kun_uz.dto.AnalyticsDTO;
import jahongir.kun_uz.entity.LmsActivityEntity;
import jahongir.kun_uz.entity.StudentEntity;
import jahongir.kun_uz.repository.LmsActivityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LmsActivityService {

    private final LmsActivityRepository lmsActivityRepository;
    private final StudentService studentService;

    // ── CRUD / Activity recording ─────────────────────────────────────────────

    public List<LmsActivityEntity> findAll() {
        return lmsActivityRepository.findAll();
    }

    public LmsActivityEntity findById(String id) {
        return lmsActivityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LmsActivity not found with ID: " + id));
    }

    /**
     * Records a new LMS activity event for a student.
     *
     * @param studentId       the student's UUID string
     * @param action          e.g. "LOGIN", "WATCH_VIDEO", "SUBMIT_HOMEWORK"
     * @param durationMinutes minutes spent on this action (nullable)
     */
    @Transactional
    public LmsActivityEntity recordActivity(String studentId, String action, Integer durationMinutes) {
        StudentEntity student = studentService.findById(studentId);

        LmsActivityEntity activity = new LmsActivityEntity();
        activity.setStudent(student);
        activity.setAction(action);
        activity.setDurationMinutes(durationMinutes);
        activity.setCreatedAt(LocalDateTime.now());
        return lmsActivityRepository.save(activity);
    }

    @Transactional
    public void delete(String id) {
        if (!lmsActivityRepository.existsById(id)) {
            throw new EntityNotFoundException("LmsActivity not found with ID: " + id);
        }
        lmsActivityRepository.deleteById(id);
    }

    // ── Analytics ─────────────────────────────────────────────────────────────

    /**
     * Returns a leaderboard of students ranked by total active minutes on the
     * LMS over the last {@code pastDays} days.
     *
     * @param pastDays number of past days to include (e.g. 7, 30)
     */
    public List<AnalyticsDTO.LmsEngagementMetric> getEngagementLeaderboard(int pastDays) {
        LocalDateTime since = LocalDateTime.now().minusDays(pastDays);
        return lmsActivityRepository.fetchEngagementLeaderboard(since);
    }

    /**
     * Returns the total (all-time) study hours summary for a specific student.
     *
     * @param studentId the student's UUID string
     */
    public Optional<AnalyticsDTO.LmsEngagementMetric> getStudentStudyHours(String studentId) {
        studentService.findById(studentId); // 404 guard
        return lmsActivityRepository.fetchStudentStudyHours(studentId);
    }

    /**
     * Convenience: returns total accumulated LMS minutes for a student.
     */
    public Long sumDurationMinutesByStudentId(String studentId) {
        return lmsActivityRepository.sumDurationMinutesByStudentId(studentId);
    }
}
