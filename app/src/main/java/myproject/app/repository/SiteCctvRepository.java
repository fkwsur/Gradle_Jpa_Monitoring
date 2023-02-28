package myproject.app.repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import myproject.app.models.SiteCctv;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public interface SiteCctvRepository extends JpaRepository<SiteCctv, String>  {
    
    SiteCctv findOneById(String id);

    List<SiteCctv> findBySiteId(String site_id);

    @Modifying(clearAutomatically = true)
    @Query(value = "delete from site_cctv where site_id = :site_id", nativeQuery = true)
    int deleteAllBySiteId(@Param("site_id") String site_id);

}
