package jahongir.kun_uz.repository;

import jahongir.kun_uz.entity.ProfileRoleEntity;
import jahongir.kun_uz.enums.Roles;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProfileRoleRepository extends CrudRepository<ProfileRoleEntity, Integer>, PagingAndSortingRepository<ProfileRoleEntity, Integer> {

    @Query("select pr.role from ProfileRoleEntity as pr where pr.profile.id =:id")
    List<Roles> getRoleListByProfileId(Integer id);

    @Transactional
    @Modifying
    @Query("DELETE from ProfileRoleEntity where profile.id =:id and role =:role")
    void deleteByIdAndRole(Integer id, Roles role);
}