package jahongir.kun_uz.repository;

import jahongir.kun_uz.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity, String> {
}
