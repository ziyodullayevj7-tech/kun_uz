package jahongir.kun_uz.repository;

import jahongir.kun_uz.entity.ArticleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ArticleRepository extends CrudRepository<ArticleEntity, Integer>, PagingAndSortingRepository<ArticleEntity, Integer> {

}
