package jahongir.kun_uz.service;

import jahongir.kun_uz.dto.AnalyticsDTO;
import jahongir.kun_uz.entity.StudentEntity;
import jahongir.kun_uz.repository.StudentAnalyticsRepository;
import jahongir.kun_uz.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentAnalyticsRepository studentAnalyticsRepository;

    // --- CRUD ---

    public List<StudentEntity> findAll() {
        return studentRepository.findAll();
    }

    public StudentEntity findById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + id));
    }

    @Transactional
    public StudentEntity create(StudentEntity student) {
        if (studentRepository.existsByStudentNumber(student.getStudentNumber())) {
            throw new IllegalArgumentException("Student ID already exists: " + student.getStudentNumber());
        }
        if (studentRepository.existsByEmail(student.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + student.getEmail());
        }
        return studentRepository.save(student);
    }

    @Transactional
    public StudentEntity update(String id, StudentEntity incoming) {
        StudentEntity current = findById(id);
        current.setFirstName(incoming.getFirstName());
        current.setLastName(incoming.getLastName());
        current.setStatus(incoming.getStatus());
        return studentRepository.save(current);
    }

    @Transactional
    public void delete(String id) {
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
    }

    // --- Analytics Read ---

    @Transactional
    public List<AnalyticsDTO.StudentSummaryAnalytics> rebuildStudentAnalytics() {
        studentAnalyticsRepository.rebuildAnalyticsTable();
        return studentAnalyticsRepository.findAll();
    }
}