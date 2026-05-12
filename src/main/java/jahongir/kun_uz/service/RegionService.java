package jahongir.kun_uz.service;

import jahongir.kun_uz.dto.RegionByLangDto;
import jahongir.kun_uz.dto.RegionDto;
import jahongir.kun_uz.entity.RegionEntity;
import jahongir.kun_uz.mapper.RegionMapper;
import jahongir.kun_uz.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;

    public RegionDto create(RegionDto dto) {
        RegionEntity entity = new RegionEntity();
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setKey(dto.getKey());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setCreatedDate(LocalDate.now());
        regionRepository.save(entity);
        dto.setId(entity.getId());
        return dto;
    }

    public Boolean update(Integer id, RegionDto dto) {
        Optional<RegionEntity> optional = regionRepository.findById(id);
        if (optional.isEmpty()){
            return false;
        }
        RegionEntity entity = optional.get();
        entity.setKey(dto.getKey());
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        regionRepository.save(entity);
        return true;
    }

    public Boolean deleteById(Integer id) {
        Optional<RegionEntity> optional = regionRepository.findById(id);
        if (optional.isEmpty()){
            return false;
        }
        RegionEntity entity = optional.get();
        entity.setVisible(Boolean.FALSE);
        return true;
    }

    public List<RegionDto> getAll() {
        List<RegionDto> dtos = new LinkedList<>();
        List<RegionMapper> mappers = regionRepository.getMappers();
        mappers.forEach(mapper -> {
            dtos.add(toDtoFromMapper(mapper));
        });
        return dtos;
    }

    public RegionDto toDtoFromMapper(RegionMapper mapper){
        RegionDto dto = new RegionDto();
        dto.setId(mapper.getId());
        dto.setOrderNumber(mapper.getOrderNumber());
        dto.setNameUz(mapper.getNameUz());
        dto.setNameRu(mapper.getNameRu());
        dto.setNameEn(mapper.getNameEn());
        dto.setKey(mapper.getKey());
        return dto;
    }

    public List<RegionByLangDto> getAllByLang(String lang) {
        List<RegionByLangDto> dtos = new LinkedList<>();
        List<RegionMapper> mappers = regionRepository.getMappers();
        if (lang.equalsIgnoreCase("uz")){
            mappers.forEach(mapper -> {
                RegionByLangDto dto = new RegionByLangDto();
                dto.setId(mapper.getId());
                dto.setKey(mapper.getKey());
                dto.setName(mapper.getNameUz());
                dtos.add(dto);
            });
        }else if (lang.equalsIgnoreCase("ru")){
            mappers.forEach(mapper -> {
                RegionByLangDto dto = new RegionByLangDto();
                dto.setId(mapper.getId());
                dto.setKey(mapper.getKey());
                dto.setName(mapper.getNameRu());
                dtos.add(dto);
            });
        }else{
            mappers.forEach(mapper -> {
                RegionByLangDto dto = new RegionByLangDto();
                dto.setId(mapper.getId());
                dto.setKey(mapper.getKey());
                dto.setName(mapper.getNameEn());
                dtos.add(dto);
            });
        }
        return dtos;
    }
}

