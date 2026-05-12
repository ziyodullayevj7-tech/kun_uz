package jahongir.kun_uz.repository;

import jahongir.kun_uz.entity.RegionEntity;
import jahongir.kun_uz.entity.SectionEntity;
import jahongir.kun_uz.mapper.RegionMapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface SectionRepository extends CrudRepository<SectionEntity, Integer>, PagingAndSortingRepository<SectionEntity, Integer> {
    @Query("select s.id as id, s.orderNumber as orderNumber, s.nameUz as nameUz, s.nameRu as nameRu, s.nameEn as nameEn, s.key as key from SectionEntity as s where s.visible is true order by s.createdDate desc")
    List<RegionMapper> getMappers();
}
