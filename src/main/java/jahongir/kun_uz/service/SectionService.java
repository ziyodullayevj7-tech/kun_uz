package jahongir.kun_uz.service;

import jahongir.kun_uz.dto.CategoryByLangDto;
import jahongir.kun_uz.dto.SectionDto;
import jahongir.kun_uz.entity.SectionEntity;
import jahongir.kun_uz.exp.AppBadException;
import jahongir.kun_uz.mapper.RegionMapper;
import jahongir.kun_uz.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class SectionService {
    @Autowired
    private SectionRepository sectionRepository;


    public SectionDto create(SectionDto dto) {
        SectionEntity entity = new SectionEntity();
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setKey(dto.getKey());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setCreatedDate(LocalDate.now());
        sectionRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public Boolean update(Integer id, SectionDto dto) {
        Optional<SectionEntity> optional = sectionRepository.findById(id);
        if (optional.isEmpty()){
            return false;
        }
        SectionEntity entity = optional.get();
        entity.setKey(dto.getKey());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        sectionRepository.save(entity);
        return true;
    }

    public Boolean deleteById(Integer id) {
        Optional<SectionEntity> optional = sectionRepository.findById(id);
        if (optional.isEmpty()){
            return false;
        }
        SectionEntity entity = optional.get();
        entity.setVisible(Boolean.FALSE);
        return true;
    }

    public List<SectionDto> getAll() {
        List<SectionDto> dtos = new LinkedList<>();
        List<RegionMapper> mappers = sectionRepository.getMappers();
        mappers.forEach(mapper -> {
            dtos.add(toDtoFromMapper(mapper));
        });
        return dtos;
    }

    public SectionDto toDtoFromMapper(RegionMapper mapper){
        SectionDto dto = new SectionDto();
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
        List<RegionMapper> mappers = sectionRepository.getMappers();
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

    public List<SectionEntity> getListById(List<Integer> sectionIds) {
        List<SectionEntity> sections = new LinkedList<>();
        sectionIds.forEach(id -> {
            Optional<SectionEntity> optional = sectionRepository.findById(id);
            optional.ifPresent(sections::add);
        });
        if (sections.isEmpty()){
            throw new AppBadException("No section found");
        }
        return sections;
    }
}
