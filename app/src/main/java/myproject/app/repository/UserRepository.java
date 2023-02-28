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

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Boolean existsByUserId(String user_id);

    Boolean existsByEmail(String email);

    User findByEmail(String email);

    User findByUserId(String user_id);
    
    User findByUserIdAndGrade(String user_id, String grade);
    
    User findByUserIdAndAppKey(String user_id, String app_key);

    User findByIdAndRefreshtoken(String id, String rxauth);

    List<User> findByIdIn(List<String> id);

    User findOneById(String id);
    
    @Query(value = "select count(*) over() as count,user.id,user.user_id,user.name,user.email,user.number,user.department,user.position,user.grade,GROUP_CONCAT(site.site_name ORDER BY site.id) site_name,GROUP_CONCAT(site.id ORDER BY site.id) site_id from user left join site on find_in_set(site.id, user.site_id) group by user.id order by user.createdAt DESC limit 20 offset :offset", nativeQuery = true)
    List<Map<String, Object>> findAllType(@Param("offset") Integer offset);

    @Query(value = "select count(*) over() as count,user.id,user.user_id,user.name,user.email,user.number,user.department,user.position,user.grade,GROUP_CONCAT(site.site_name ORDER BY site.id) site_name,GROUP_CONCAT(site.id ORDER BY site.id) site_id from user left join site ON find_in_set(site.id, user.site_id) where user.grade = :grade group by user.id order by user.createdAt DESC limit 20 offset :offset", nativeQuery = true)
    List<Map<String, Object>> findType(@Param("grade") String grade, @Param("offset") Integer offset);

    void deleteByUserId(String decoded);

    @Modifying(clearAutomatically = true)
    @Query(value = "update user set grade = :grade where id in (:id)", nativeQuery = true)
    int BulkUpdateInfo(@Param("grade") String authority, @Param("id") List<String> id);

    @Modifying(clearAutomatically = true)
    @Query(value = " delete from user where id in (:id)", nativeQuery = true)
    int BulkDeleteInfo(@Param("id") List<String> id);

    @Query(value = "select count(*) over() as count,user.id,user.user_id,user.name,user.email,user.number,user.department,user.position,user.grade,group_concat(site.site_name ORDER BY site.id) site_name,group_concat(site.id ORDER BY site.id) site_id from user left join site on find_in_set(site.id, user.site_id) where user.name like %:keywords% group by user.id order by user.name asc limit 20 offset :offset", nativeQuery = true)
    List<Map<String, Object>> SearchUserName(@Param("keywords") String keywords, @Param("offset") Integer offset);

    @Modifying(clearAutomatically = true)
    @Query(value = "update user set site_id = :site_id where id in (:id)", nativeQuery = true)
    int UpdateUserSite(@Param("site_id") String site_id, @Param("id") List<String> id);

}
