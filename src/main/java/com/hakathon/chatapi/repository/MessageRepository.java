package com.hakathon.chatapi.repository;

import com.hakathon.chatapi.model.ChatEntity;
import com.hakathon.chatapi.model.MessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<MessageEntity, String> {
    List<MessageEntity> findAllByChatId(String chatId);
}
