package jahongir.kun_uz.repository;

import jahongir.kun_uz.entity.ProfileRoleEntity;
import jahongir.kun_uz.mapper.ProfileRoleMapper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ProfileRoleRepository extends CrudRepository<ProfileRoleEntity, Integer>, PagingAndSortingRepository<ProfileRoleEntity, Integer> {
    @Query("select pr.id as id, pr.profile.id as profileId, pr.profile.name as profileName, role as role from ProfileRoleEntity as pr where pr.profile.id =:profile_id and pr.visible is true")
    List<ProfileRoleMapper> getByProfileId(Integer profile_id);

    @Query("from ProfileRoleEntity as pr where pr.profile.id =:id and pr.visible is true")
    List<ProfileRoleEntity> getListByProfileId(Integer id);

    @Query("from ProfileRoleEntity as pr where pr.id =:id and pr.visible is true")
    ProfileRoleEntity getEntityByProfileId(Integer id);

    @Query("select pr.id as id, pr.profile.id as profileId, pr.profile.name as profileName, role as role from ProfileRoleEntity as pr where pr.profile.id in:ids and pr.visible is true")
    List<ProfileRoleMapper> getByProfileIds(List<Integer> ids);
}