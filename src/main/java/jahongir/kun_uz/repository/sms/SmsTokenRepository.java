package jahongir.kun_uz.repository.sms;

import jahongir.kun_uz.entity.sms.SmsTokenEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SmsTokenRepository extends CrudRepository<SmsTokenEntity, Integer> {
    @Query("from SmsTokenEntity order by createdDateAndTime desc limit 1")
    Optional<SmsTokenEntity> findTopByOrderByCreatedDateTimeDesc();
}
