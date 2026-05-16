package jahongir.kun_uz.service;

import jahongir.kun_uz.dto.profile.*;
import jahongir.kun_uz.entity.ProfileEntity;
import jahongir.kun_uz.enums.Status;
import jahongir.kun_uz.exp.AppBadException;
import jahongir.kun_uz.repository.ProfileRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ProfileRoleService profileRoleService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public ProfileDto create(ProfileDto dto){
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleIsTrue(dto.getUsername());
        if (optional.isPresent()){
            throw new AppBadException("User exists");
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());

        entity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        entity.setUsername(dto.getUsername());
        entity.setStatus(Status.ACTIVE);
        entity.setVisible(Boolean.TRUE);
        profileRepository.save(entity);

        profileRoleService.create(entity.getId(), dto.getRoles());
        return toDtoFromEntity(entity);
    }

    public ProfileDto toDtoFromEntity(ProfileEntity entity){
        ProfileDto dto = new ProfileDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setUsername(entity.getUsername());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public ProfileDto update(Integer id, ProfileUpdateDto dto){
        ProfileEntity entity = getEntityById(id);
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleIsTrue(dto.getUsername());
        if (optional.isPresent()){
            throw new AppBadException("Username already exists");
        }

        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setUsername(dto.getUsername());
        profileRepository.save(entity);
        //role_save
        profileRoleService.merge(entity.getId(), dto.getRolesList());
        //result
        ProfileDto response = toDtoFromEntity(entity);
        response.setRoles(dto.getRolesList());
        return response;
    }

    public ProfileEntity getEntityById(Integer id){
        return profileRepository.findByIdAndVisibleIsTrue(id).orElseThrow(() -> new AppBadException("Profile not found"));
    }

    public ProfileDto getDtoById(Integer id) {
        ProfileEntity entity = getEntityById(id);
        ProfileDto dto = toDtoFromEntity(entity);
        dto.setRoles(profileRoleService.getByProfileId(id));
        return dto;
    }

    public ProfileDto updateDetail(Integer currentProfileId, @Valid ProfileUpdateDetailDto dto) {
        ProfileEntity entity = getEntityById(currentProfileId);
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        profileRepository.save(entity);
        return toDtoFromEntity(entity);
    }

    public PageImpl<ProfileDto> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<ProfileEntity> result = profileRepository.findAllWithRoles(pageable);

        List<ProfileDto> dtoList = new LinkedList<>();
        for (ProfileEntity entity : result.getContent()) {
            dtoList.add(toDtoFromEntity(entity));
        }
        return new PageImpl<>(dtoList, pageable, result.getTotalElements());
    }

    public Boolean deleteById(Integer id) {
        ProfileEntity entity = getEntityById(id);
        entity.setVisible(false);
        profileRepository.save(entity);
        return true;
    }

    public Boolean updatePhotoId(Integer currentProfileId, @Valid ProfileUpdatePhototDto dto) {
        ProfileEntity entity = getEntityById(currentProfileId);
        entity.setPhotoId(dto.getPhotoId());
        profileRepository.save(entity);
        return true;
    }

    public Boolean updatePassword(Integer currenProfileId, @Valid ProfileUpdatePasswordDto dto) {
        ProfileEntity entity = getEntityById(currenProfileId);
        if (!bCryptPasswordEncoder.matches(dto.getCurrentPassword(), entity.getPassword())){
            throw new AppBadException("Wrong password");
        }
        entity.setPassword(bCryptPasswordEncoder.encode(dto.getNewPassword()));
        profileRepository.save(entity);
        return true;
    }
}
