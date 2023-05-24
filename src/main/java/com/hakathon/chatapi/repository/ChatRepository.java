package com.hakathon.chatapi.repository;

import com.hakathon.chatapi.model.ChatEntity;
import com.hakathon.chatapi.model.UserExtra;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends MongoRepository<ChatEntity, String> {

    Optional<ChatEntity> findById(long id);

    List<ChatEntity> findAllByClientId(String clientId);
}
