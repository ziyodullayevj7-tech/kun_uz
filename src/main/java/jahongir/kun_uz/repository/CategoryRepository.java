package jahongir.kun_uz.repository;

import jahongir.kun_uz.entity.CategoryEntity;
import jahongir.kun_uz.mapper.RegionMapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CategoryRepository extends CrudRepository<CategoryEntity, Integer>, PagingAndSortingRepository<CategoryEntity, Integer> {
    @Query("select c.id as id, c.orderNumber as ocdecNumbec, c.nameUz as nameUz, c.nameRu as nameRu, c.nameEn as nameEn, c.key as key from CategoryEntity as c where c.visible is true order by c.orderNumber desc")
    List<RegionMapper> getMappers();
}
