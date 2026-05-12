package jahongir.kun_uz.repository;

import jahongir.kun_uz.entity.ProfileEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer>, PagingAndSortingRepository<ProfileEntity, Integer> {
    @Query("from ProfileEntity as p order by p.created_date desc")
    List<ProfileEntity> geAll();

    @Query("select p.id from ProfileEntity as p where p.name like:name ")
    Optional<List<Integer>> getIdByName(String name);
}
