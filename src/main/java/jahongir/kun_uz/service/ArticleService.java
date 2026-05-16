package jahongir.kun_uz.service;

import jahongir.kun_uz.dto.article.*;
import jahongir.kun_uz.entity.ArticleEntity;
import jahongir.kun_uz.entity.CategoryEntity;
import jahongir.kun_uz.entity.SectionEntity;
import jahongir.kun_uz.exp.AppBadException;
import jahongir.kun_uz.repository.ArticleRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
        ArticleEntity entity = findArticleById(articleId);
        return toDtoFromEntity(entity);
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

    public ArticleUpdateDto update(Integer id, ArticleUpdateDto dto) {
        ArticleEntity entity = findArticleById(id);
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setContent(dto.getContent());
        entity.setImageId(dto.getImageId());
        entity.setRegionId(dto.getRegionId());
        List<CategoryEntity> categories = categoryService.getListById(dto.getCategoryIds());//get categories by ids
        List<SectionEntity> sections = sectionService.getListById(dto.getSectionIds());//get sections by ids

        entity.setCategories(categories);//set found categories
        entity.setSections(sections);//set found sections
        articleRepository.save(entity);
        return dto;
    }

    public Boolean deleteById(Integer id) {
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        if (optional.isEmpty()){
            throw new AppBadException("Article not found");
        }
        ArticleEntity entity = optional.get();
        entity.setVisible(false);
        articleRepository.save(entity);
        return true;
    }

    public Boolean changeStatusById(ArticleStatusDto dto) {
        ArticleEntity entity = findArticleById(dto.getArticleId());
        if (entity.getVisible().equals(true)){
            entity.setStatus(dto.getStatus());
            articleRepository.save(entity);
            return true;
        }
        return false;
    }

    public ArticleEntity findArticleById(Integer id){
        Optional<ArticleEntity> optional = articleRepository.findById(id);
        if (optional.isEmpty()){
            throw new AppBadException("Article not found");
        }
        return optional.get();
    }

    public PageImpl<ArticleShortInfoDto> paginationBySectionId(int page, int size, Integer sectionId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<ArticleEntity> result = articleRepository.findAllBySectionId(pageable, sectionId);

        List<ArticleShortInfoDto> dtos = new LinkedList<>();
        result.getContent().forEach(entity -> dtos.add(toShortDtoFromEntity(entity)));
        return new PageImpl<>(dtos, pageable, result.getTotalElements());
    }

    public ArticleShortInfoDto toShortDtoFromEntity(ArticleEntity entity){
        ArticleShortInfoDto dto = new ArticleShortInfoDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setContent(entity.getContent());
        dto.setDescription(entity.getDescription());
        return dto;
    }

    public PageImpl<ArticleShortInfoDto> paginationExceptForIds(int page, ArticleExceptIdsDto dto) {
        Pageable pageable = PageRequest.of(page, 12, Sort.by("createdDate").descending());
        Page<ArticleEntity> result = articleRepository.findAllExceptIds(pageable, dto.getIds());

        List<ArticleShortInfoDto> dtos = new LinkedList<>();
        result.getContent().forEach(entity -> dtos.add(toShortDtoFromEntity(entity)));
        return new PageImpl<>(dtos, pageable, result.getTotalElements());
    }

    public PageImpl<ArticleShortInfoDto> paginationByCategoryId(int page, int size, Integer categoryId) {
        Pageable pageable = PageRequest.of(page, 12, Sort.by("createdDate").descending());
        Page<ArticleEntity> result = articleRepository.findAllByCategoryId(pageable, size, categoryId);

        List<ArticleShortInfoDto> dtos = new LinkedList<>();
        result.getContent().forEach(entity -> dtos.add(toShortDtoFromEntity(entity)));
        return new PageImpl<>(dtos, pageable, result.getTotalElements());
    }
}
