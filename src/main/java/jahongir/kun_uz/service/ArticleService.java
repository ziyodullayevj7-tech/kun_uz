package jahongir.kun_uz.service;

import jahongir.kun_uz.dto.ArticleDto;
import jahongir.kun_uz.entity.ArticleEntity;
import jahongir.kun_uz.entity.CategoryEntity;
import jahongir.kun_uz.entity.SectionEntity;
import jahongir.kun_uz.exp.AppBadException;
import jahongir.kun_uz.repository.ArticleRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SectionService sectionService;

    public ArticleDto create(@Valid ArticleDto dto) {
        ArticleEntity entity = new ArticleEntity();
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setDescription(dto.getDescription());
        entity.setImageId(dto.getImageId());
        entity.setRegionId(dto.getRegionId());
        List<CategoryEntity> categories = categoryService.getListById(dto.getCategoryIds());//get categories by ids
        List<SectionEntity> sections = sectionService.getListById(dto.getSectionIds());//get sections by ids

        entity.setCategories(categories);//set found categories
        entity.setSections(sections);//set found sections
        articleRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public ArticleDto getById(Integer articleId) {
        Optional<ArticleEntity> optional = articleRepository.findById(articleId);
        if (optional.isEmpty()){
            throw new AppBadException("Article not found");
        }
        return toDtoFromEntity(optional.get());
    }

    public ArticleDto toDtoFromEntity(ArticleEntity entity){
        ArticleDto dto = new ArticleDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setContent(dto.getContent());
        dto.setImageId(dto.getImageId());
        dto.setRegionId(entity.getRegionId());

        //set category ids
        List<Integer> categoryIds = new LinkedList<>();
        entity.getCategories().forEach(category -> categoryIds.add(category.getId()));
        dto.setCategoryIds(categoryIds);

        //set section ids
        List<Integer> sectionIds = new LinkedList<>();
        entity.getSections().forEach(section -> sectionIds.add(section.getId()));
        dto.setSectionIds(sectionIds);

        return dto;
    }
}
