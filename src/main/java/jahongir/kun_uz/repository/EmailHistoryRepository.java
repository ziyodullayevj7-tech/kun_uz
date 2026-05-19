package jahongir.kun_uz.repository;

import jahongir.kun_uz.entity.EmailHistoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface EmailHistoryRepository extends CrudRepository<EmailHistoryEntity, Integer> {
    @Query("from EmailHistoryEntity as eh where eh.toAccount like :username order by eh.createdDateAndTime desc limit 1")
    Optional<EmailHistoryEntity> findLastByUsername(String username);

    @Query("from EmailHistoryEntity where toAccount like :email")
    Optional<List<EmailHistoryEntity>> getEmailHistoryEntitiesByToAccount(String email);
}
