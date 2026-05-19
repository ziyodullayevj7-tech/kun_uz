package jahongir.kun_uz.service;

import jahongir.kun_uz.entity.ProfileRoleEntity;
import jahongir.kun_uz.enums.Roles;
import jahongir.kun_uz.repository.ProfileRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileRoleService {
    @Autowired
    private ProfileRoleRepository profileRoleRepository;

    public void create(Integer id, List<Roles> roles) {
        for (Roles role : roles) {
            ProfileRoleEntity entity = new ProfileRoleEntity();
            entity.setProfileId(id);
            entity.setVisible(Boolean.TRUE);
            entity.setRole(role);
            profileRoleRepository.save(entity);
        }
    }

    public void create(Integer profileId, Roles role){
        ProfileRoleEntity entity = new ProfileRoleEntity();
        entity.setProfileId(profileId);
        entity.setRole(role);
        profileRoleRepository.save(entity);
    }

    public void merge(Integer id, List<Roles> newRoleList) {
        List<Roles> oldList = profileRoleRepository.getRoleListByProfileId(id);
        newRoleList.stream().filter(n -> !oldList.contains(n)).forEach(role -> create(id, role));
        oldList.stream().filter(old -> !newRoleList.contains(old)).forEach(role -> profileRoleRepository.deleteByIdAndRole(id, role));
    }

    public List<Roles> getByProfileId(Integer id) {
        return profileRoleRepository.getRoleListByProfileId(id);
    }

    public void deleteRolesByProfileId(Integer id) {
        profileRoleRepository.deleteByProfileId(id);
    }
}
