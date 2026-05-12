package jahongir.kun_uz.repository;

import jahongir.kun_uz.entity.RegionEntity;
import jahongir.kun_uz.mapper.RegionMapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RegionRepository extends CrudRepository<RegionEntity, Integer>, PagingAndSortingRepository<RegionEntity, Integer> {
    @Query("select r.id as id, r.orderNumber as orderNumber, r.nameUz as nameUz, r.nameRu as nameRu, r.nameEn as nameEn, r.key as key from RegionEntity as r where r.visible is true order by r.createdDate desc")
    List<RegionMapper> getMappers();
}
