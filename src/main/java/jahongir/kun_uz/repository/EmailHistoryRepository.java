package jahongir.kun_uz.repository;

import jahongir.kun_uz.entity.EmailHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity, Integer>, PagingAndSortingRepository<EmailHistoryEntity, Integer> {
    @Query("from EmailHistoryEntity as eh where eh.toAccount like :username order by eh.createdDateAndTime desc limit 1")
    Optional<EmailHistoryEntity> findLastByUsername(String username);

    @Query("from EmailHistoryEntity where toAccount like :email")
    Optional<List<EmailHistoryEntity>> getEmailHistoryEntitiesByToAccount(String email);

    @Query("from EmailHistoryEntity where date(createdDateAndTime) =:date")
    Optional<List<EmailHistoryEntity>> getEmailHistoryEntitiesByGivenDate(LocalDate date);
}
