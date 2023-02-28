package myproject.app.repository;

import myproject.app.models.Chatting;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChattingRepository extends MongoRepository<Chatting, String> {

    List<Chatting> findByRoomNameOrderByTimeDesc(String roomCode,Pageable pageable);
    
}