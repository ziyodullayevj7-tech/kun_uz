package jahongir.kun_uz.service;

import jahongir.kun_uz.dto.CategoryByLangDto;
import jahongir.kun_uz.dto.CategoryDto;
import jahongir.kun_uz.entity.CategoryEntity;
import jahongir.kun_uz.exp.AppBadException;
import jahongir.kun_uz.mapper.RegionMapper;
import jahongir.kun_uz.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDto create(CategoryDto dto) {
        CategoryEntity entity = new CategoryEntity();
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setKey(dto.getKey());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setCreatedDate(LocalDate.now());
        categoryRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public Boolean update(Integer id, CategoryDto dto) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty()){
            return false;
        }
        CategoryEntity entity = optional.get();
        entity.setKey(dto.getKey());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        categoryRepository.save(entity);
        return true;
    }

    public Boolean deleteById(Integer id) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isEmpty()){
            return false;
        }
        CategoryEntity entity = optional.get();
        entity.setVisible(Boolean.FALSE);
        return true;
    }

    public List<CategoryDto> getAll() {
        List<CategoryDto> dtos = new LinkedList<>();
        List<RegionMapper> mappers = categoryRepository.getMappers();
        mappers.forEach(mapper -> {
            dtos.add(toDtoFromMapper(mapper));
        });
        return dtos;
    }

    public CategoryDto toDtoFromMapper(RegionMapper mapper){
        CategoryDto dto = new CategoryDto();
        dto.setId(mapper.getId());
        dto.setOrderNumber(mapper.getOrderNumber());
        dto.setNameUz(mapper.getNameUz());
        dto.setNameRu(mapper.getNameRu());
        dto.setNameEn(mapper.getNameEn());
        dto.setKey(mapper.getKey());
        return dto;
    }

    public List<CategoryByLangDto> getAllByLang(String lang) {
        List<CategoryByLangDto> dtos = new LinkedList<>();
        List<RegionMapper> mappers = categoryRepository.getMappers();
        if (lang.equalsIgnoreCase("uz")){
            mappers.forEach(mapper -> {
                CategoryByLangDto dto = new CategoryByLangDto();
                dto.setId(mapper.getId());
                dto.setKey(mapper.getKey());
                dto.setName(mapper.getNameUz());
                dtos.add(dto);
            });
        }else if (lang.equalsIgnoreCase("ru")){
            mappers.forEach(mapper -> {
                CategoryByLangDto dto = new CategoryByLangDto();
                dto.setId(mapper.getId());
                dto.setKey(mapper.getKey());
                dto.setName(mapper.getNameRu());
                dtos.add(dto);
            });
        }else{
            mappers.forEach(mapper -> {
                CategoryByLangDto dto = new CategoryByLangDto();
                dto.setId(mapper.getId());
                dto.setKey(mapper.getKey());
                dto.setName(mapper.getNameEn());
                dtos.add(dto);
            });
        }
        return dtos;
    }

    public List<CategoryEntity> getListById(List<Integer> categoryIds) {
        List<CategoryEntity> categories = new LinkedList<>();
        categoryIds.forEach(id -> {
            Optional<CategoryEntity> optional = categoryRepository.findById(id);
            optional.ifPresent(categories::add);
        });
        if (categories.isEmpty()){
            throw new AppBadException("No category found");
        }
        return categories;
    }
}
