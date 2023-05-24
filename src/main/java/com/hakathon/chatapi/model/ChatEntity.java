package com.hakathon.chatapi.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "chat")
public class ChatEntity {
    @Id
    private long id;

    private String clientId;
    private String managerId;
}