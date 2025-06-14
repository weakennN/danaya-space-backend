package danayaspace.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    @Query("select e from UserEntity e where e.email = :email")
    Optional<UserEntity> findByEmail(String email);
}
