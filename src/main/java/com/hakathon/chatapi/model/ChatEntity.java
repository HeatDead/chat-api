package com.hakathon.chatapi.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "chat")
public class ChatEntity {
    @Id
    private String id;

    private String chatName;
    private ChatStatus chatStatus;

    private String clientId;
    private String managerId;
}