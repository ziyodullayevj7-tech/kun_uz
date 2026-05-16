package jahongir.kun_uz.repository;

import jahongir.kun_uz.entity.ProfileEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProfileRepository extends CrudRepository<ProfileEntity, Integer>, PagingAndSortingRepository<ProfileEntity, Integer> {

    @Query("from ProfileEntity as p where p.username like :username and p.visible is true ")
    Optional<ProfileEntity> findByUsernameAndVisibleIsTrue(@NotBlank(message = "Email is required") @Email(message = "Wrong email") String username);

    @Query("from ProfileEntity as p where p.id =:id and p.visible is true")
    Optional<ProfileEntity> findByIdAndVisibleIsTrue(Integer id);

    @Query("from ProfileEntity as p inner join fetch p.roleList where p.visible is true ")
    Page<ProfileEntity> findAllWithRoles(Pageable pageable);
}
