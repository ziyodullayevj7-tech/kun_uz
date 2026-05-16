package jahongir.kun_uz.repository;

import jahongir.kun_uz.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ArticleRepository extends CrudRepository<ArticleEntity, Integer>, PagingAndSortingRepository<ArticleEntity, Integer> {

    @Query("select a from ArticleEntity as a inner join a.sections as s where s.id =:sectionId")
    Page<ArticleEntity> findAllBySectionId(Pageable pageable, Integer sectionId);
}
