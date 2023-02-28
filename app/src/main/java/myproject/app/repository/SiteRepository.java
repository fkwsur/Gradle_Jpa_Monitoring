package myproject.app.repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import myproject.app.models.User;
import myproject.app.models.Site;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public interface SiteRepository extends JpaRepository<Site, String>  {

    List<Site> findByIdIn(List<String> id);

    @Query(value = "select count(*) over() as count, count(site_cctv.site_id) AS cctv_count, site.id, site.site_name, site.address, site.number, site.company from site left outer join site_cctv on site.id = site_cctv.site_id group by site.id order by site.createdAt DESC limit 20 offset :offset", nativeQuery = true)
    List<Map<String, Object>> GetSiteList(@Param("offset") Integer offset);

    List<Site> findAllByOrderBySiteName();

    Site findOneById(String id);

    @Modifying(clearAutomatically = true)
    @Query(value = "update user set site_id = regexp_replace(site_id, :reid, '') where site_id like :likeid", nativeQuery = true)
    int UpdateUserSite(@Param("reid") String reId, @Param("likeid") String likeId);

    @Modifying(clearAutomatically = true)
    @Query(value = "update user set grade = :grade where site_id = :site_id and grade not in (:admin)", nativeQuery = true)
    int UpdateGrade(@Param("grade") String grade, @Param("site_id") String site_id, @Param("admin") String admin);

    @Query(value = "select count(*) over() as count,count(site_cctv.site_id) AS cctv_count,site.id,site.site_name,site.address,site.number,site.company from site left outer join site_cctv on site.id = site_cctv.site_id where site.site_name like :keywords group by site.id order by site.site_name asc limit 20 offset :offset", nativeQuery = true)
    List<Map<String, Object>> SearchSiteName(@Param("keywords") String keywords, @Param("offset") Integer offset);

    @Query(value = "select *,count(*) over() as count from replay where site_id = :id order by createdAt desc limit 20 offset :offset", nativeQuery = true)
    List<Map<String, Object>> findAndCountAll(@Param("id") String id, @Param("offset") Integer offset);
    
}
