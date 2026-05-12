package jahongir.kun_uz.service;

import jahongir.kun_uz.dto.ProfileDto;
import jahongir.kun_uz.dto.ProfileRoleDto;
import jahongir.kun_uz.entity.ProfileEntity;
import jahongir.kun_uz.entity.ProfileRoleEntity;
import jahongir.kun_uz.enums.Roles;
import jahongir.kun_uz.mapper.ProfileRoleMapper;
import jahongir.kun_uz.repository.ProfileRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class ProfileRoleService {
    @Autowired
    private ProfileRoleRepository profileRoleRepository;

    public void createByProfileAndRoles(ProfileEntity entity, List<String> roles) {
        List<Roles> enums = roles.stream().map(Roles::valueOf).toList();
        enums.forEach(role -> {
            ProfileRoleEntity prEntity = new ProfileRoleEntity();
            prEntity.setRole(role);
            prEntity.setProfile(entity);
            profileRoleRepository.save(prEntity);
        });
    }

    public ProfileRoleDto getByProfileId(Integer profile_id) {
        List<ProfileRoleMapper> mappers = profileRoleRepository.getByProfileId(profile_id);
        return toDto(mappers);
    }

    public ProfileRoleDto toDto(List<ProfileRoleMapper> mappers){
        if (mappers.isEmpty()){
            throw new IllegalArgumentException("mapper list is null");
        }
        List<String> roles = mappers.stream().map(ProfileRoleMapper::getRole).toList();

        ProfileRoleDto dto = new ProfileRoleDto();
        dto.setProfileName(mappers.getFirst().getProfileName());
        dto.setId(mappers.getFirst().getId());
        dto.setRoles(roles);
        return dto;
    }

    public void update(List<String> roles, ProfileEntity profile) {
        List<ProfileRoleEntity> entities = profileRoleRepository.getListByProfileId(profile.getId());
        entities.forEach(entity -> {
            entity.setVisible(Boolean.FALSE);
            profileRoleRepository.save(entity);
        });

        roles.forEach(role -> {
            ProfileRoleEntity entity = new ProfileRoleEntity();
            entity.setProfile(profile);
            entity.setRole(Roles.valueOf(role));
            profileRoleRepository.save(entity);
        });
    }

    public void deleteByProfileId(Integer id) {
        ProfileRoleEntity entity = profileRoleRepository.getEntityByProfileId(id);
        entity.setVisible(Boolean.FALSE);
        profileRoleRepository.save(entity);
    }

    public List<ProfileRoleDto> getListByProfileId(List<Integer> ids) {
        List<ProfileRoleDto> dtos = new LinkedList<>();
        List<ProfileRoleMapper> mappers = profileRoleRepository.getByProfileIds(ids);
        dtos.add(toDto(mappers));
        return dtos;
    }//muammo bor
}
