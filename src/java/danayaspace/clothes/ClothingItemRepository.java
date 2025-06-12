package danayaspace.clothes;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ClothingItemRepository extends CrudRepository<ClothingItemEntity, Long> {

    @Query("select e from ClothingItemEntity e where e.userId = :userId")
    List<ClothingItemEntity> findByUserId(@Param("userId") Long userId);
}
