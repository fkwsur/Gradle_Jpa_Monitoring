package myproject.app.repository;

import myproject.app.models.CarliftLog;
import myproject.app.models.ExcelCarLiftLog;
import myproject.app.models.ExcelRiskAlarm;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CarliftLogRepository extends MongoRepository<CarliftLog, String> {

    List<CarliftLog> findBySiteName(String Site_Name);
    List<CarliftLog> findAll();
    List<CarliftLog> findBySiteNameOrderByTimeDesc(String Site_Name,Pageable pageable);

    List<ExcelCarLiftLog> findBySiteNameOrderByTimeDesc(String Site_Name);

    List<ExcelRiskAlarm> findBySiteNameInOrderByTimeDesc(String Site_Name);
    
}
