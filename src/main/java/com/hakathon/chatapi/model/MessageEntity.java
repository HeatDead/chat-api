package com.hakathon.chatapi.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "message")
public class MessageEntity {
    @Id
    private String id;

    private String text;
    private long chatId;
    private String senderId;
}