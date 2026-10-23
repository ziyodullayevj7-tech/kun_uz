package jahongir.kun_uz.service;

import jahongir.kun_uz.dto.AnalyticsDTO;
import jahongir.kun_uz.entity.FaceIdLogEntity;
import jahongir.kun_uz.entity.StudentEntity;
import jahongir.kun_uz.repository.FaceIdLogsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FaceIdLogService {

    private final FaceIdLogsRepository faceIdLogsRepository;
    private final StudentService studentService;

    // ── CRUD / Log recording ──────────────────────────────────────────────────

    public List<FaceIdLogEntity> findAll() {
        return faceIdLogsRepository.findAll();
    }

    public FaceIdLogEntity findById(String id) {
        return faceIdLogsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("FaceIdLog not found with ID: " + id));
    }

    /**
     * Records a new campus-access event (either "IN" or "OUT") for a student.
     *
     * @param studentId the student's UUID string
     * @param location  the gate / location name (e.g. "Main Gate")
     * @param direction "IN" or "OUT"
     */
    @Transactional
    public FaceIdLogEntity recordEntry(String studentId, String location, String direction) {
        if (!"IN".equals(direction) && !"OUT".equals(direction)) {
            throw new IllegalArgumentException("Direction must be 'IN' or 'OUT', got: " + direction);
        }
        StudentEntity student = studentService.findById(studentId);

        FaceIdLogEntity log = new FaceIdLogEntity();
        log.setStudent(student);
        log.setLocation(location);
        log.setDirection(direction);
        log.setTimestamp(LocalDateTime.now());
        return faceIdLogsRepository.save(log);
    }

    @Transactional
    public void delete(String id) {
        if (!faceIdLogsRepository.existsById(id)) {
            throw new EntityNotFoundException("FaceIdLog not found with ID: " + id);
        }
        faceIdLogsRepository.deleteById(id);
    }

    // ── Analytics ─────────────────────────────────────────────────────────────

    /**
     * Returns a leaderboard of students ordered by campus-entry count over the
     * last {@code pastDays} days.
     *
     * @param pastDays number of past days to include (e.g. 30)
     */
    public List<AnalyticsDTO.FaceIdFrequencyMetric> getAttendanceLeaderboard(int pastDays) {
        LocalDateTime since = LocalDateTime.now().minusDays(pastDays);
        return faceIdLogsRepository.fetchAttendanceLeaderboard(since);
    }

    /**
     * Returns the IN/OUT ratio for a specific student as a simple map.
     * Keys are "IN" and "OUT"; values are the counts.
     *
     * @param studentId the student's UUID string
     * @return map with keys "IN" and "OUT"
     */
    public Map<String, Long> getStudentAccessRatio(String studentId) {
        // Verify student exists
        studentService.findById(studentId);

        List<Object[]> rows = faceIdLogsRepository.fetchDirectionCountsForStudent(studentId);
        return rows.stream().collect(
                Collectors.toMap(
                        row -> (String) row[0],
                        row -> (Long) row[1]
                )
        );
    }
}
