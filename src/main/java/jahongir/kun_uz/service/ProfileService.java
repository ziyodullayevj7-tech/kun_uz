package jahongir.kun_uz.service;

import jahongir.kun_uz.dto.ProfileDto;
import jahongir.kun_uz.dto.ProfileRoleDto;
import jahongir.kun_uz.entity.ProfileEntity;
import jahongir.kun_uz.enums.Status;
import jahongir.kun_uz.mapper.ProfileRoleMapper;
import jahongir.kun_uz.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired ProfileRoleService profileRoleService;

    public ProfileDto create(ProfileDto dto) {
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setStatus(Status.ACTIVE);
        entity.setPassword(dto.getPassword());
        entity.setUsername(dto.getUsername());
        entity.setCreated_date(LocalDate.now());
        entity.setPhoto_id(UUID.randomUUID().toString());

        profileRepository.save(entity);
        profileRoleService.createByProfileAndRoles(entity, dto.getRoles());
        dto.setId(entity.getId());
        dto.setPhoto_id(entity.getPhoto_id());
        dto.setVisible(entity.getVisible());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public List<ProfileDto> getAll() {
        List<ProfileEntity> entities = profileRepository.geAll();
        List<ProfileDto> dtos = new LinkedList<>();
        entities.forEach(entity -> {
            ProfileDto dto = toDto(entity);
            dtos.add(dto);
        });
        return dtos;
    }

    public ProfileDto toDto(ProfileEntity entity){
        ProfileDto dto = new ProfileDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setPassword(entity.getPassword());
        dto.setStatus(entity.getStatus());
        dto.setPhoto_id(entity.getPhoto_id());
        dto.setUsername(entity.getUsername());
        return dto;
    }

    public Boolean update(ProfileDto dto, Integer id) {
        Optional<ProfileEntity> optional = profileRepository.findById(id);
        if (optional.isEmpty()){
            return false;
        }
        ProfileEntity entity = optional.get();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setPhoto_id(dto.getPhoto_id());
        profileRoleService.update(dto.getRoles(), entity);
        profileRepository.save(entity);
        return true;
    }

    public Boolean delete(Integer id) {
        Optional<ProfileEntity> optional = profileRepository.findById(id);
        if (optional.isEmpty()){
            return false;
        }
        ProfileEntity entity = optional.get();
        entity.setVisible(Boolean.FALSE);
        profileRoleService.deleteByProfileId(id);
        profileRepository.save(entity);
        return true;
    }

    public ProfileDto getById(Integer id) {
        Optional<ProfileEntity> optional = profileRepository.findById(id);
        if (optional.isEmpty()){
            throw new IllegalArgumentException("Profile not found with this id");
        }
        return toDto(optional.get());
    }

    public List<ProfileRoleDto> getByName(String name) {
        Optional<List<Integer>> optional = profileRepository.getIdByName(name);
        if (optional.isEmpty()){
            throw new IllegalArgumentException("No user found with this name");
        }
        return profileRoleService.getListByProfileId(optional.get());
    }
}
