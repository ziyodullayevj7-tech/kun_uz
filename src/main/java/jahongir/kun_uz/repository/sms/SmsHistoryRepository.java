package jahongir.kun_uz.repository.sms;

import jahongir.kun_uz.entity.sms.SmsHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SmsHistoryRepository extends CrudRepository<SmsHistoryEntity, Integer>, PagingAndSortingRepository<SmsHistoryEntity, Integer> {
    Optional<SmsHistoryEntity> findByPhoneNumber(String phoneNumber);

    @Query("from SmsHistoryEntity where date(createdDateAndTime) =:createdDate")
    Optional<List<SmsHistoryEntity>> getByCreatedDate(LocalDate createdDate);
}
