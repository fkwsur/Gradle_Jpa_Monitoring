package myproject.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import myproject.app.models.Site;

public interface SiteCctvRepositoryImpl extends JpaRepository<Site, Long>{
    
}
