package myproject.app.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import myproject.app.models.CarliftLog;
import myproject.app.models.ExcelCarLiftLog;
import myproject.app.models.ExcelRiskAlarm;
import myproject.app.models.Chatting;
import myproject.app.repository.CarliftLogRepository;
import myproject.app.repository.ChattingRepository;

@Service
@Transactional(readOnly = true)
public class MongodbService {
    private final CarliftLogRepository carliftLogRepository;
    private final ChattingRepository chattingRepository;

    @Autowired
    public MongodbService(CarliftLogRepository carliftLogRepository,ChattingRepository chattingRepository) {
        this.carliftLogRepository = carliftLogRepository;
        this.chattingRepository = chattingRepository;
    }

    public List<CarliftLog> findAll(String name) throws Exception {
        try {
            List<CarliftLog> log = carliftLogRepository.findAll();
            System.out.println("???????????????????");
            System.out.println(log);
            return log;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<CarliftLog> RealTimeLog(String Site_Name,Integer offset) throws Exception {
        try {
            Pageable pageable = PageRequest.of(offset, 20);
            List<CarliftLog> log = carliftLogRepository.findBySiteNameOrderByTimeDesc(Site_Name,pageable);
            return log;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<ExcelCarLiftLog> ExcelCarLiftLog(String Site_Name) throws Exception {
        try {
            List<ExcelCarLiftLog> log = carliftLogRepository.findBySiteNameOrderByTimeDesc(Site_Name);
            return log;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<ExcelRiskAlarm> ExcelRiskAlarm(String Site_Name) throws Exception {
        try {
            List<ExcelRiskAlarm> log = carliftLogRepository.findBySiteNameInOrderByTimeDesc(Site_Name);
            return log;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<Chatting> GetChatting(String roomCode,Integer offset) throws Exception {
        try {
            Pageable pageable = PageRequest.of(offset, 20);
            List<Chatting> log = chattingRepository.findByRoomNameOrderByTimeDesc(roomCode,pageable);
            return log;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
    
}
