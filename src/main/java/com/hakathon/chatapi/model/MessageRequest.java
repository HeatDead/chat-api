package com.hakathon.chatapi.model;

import lombok.Data;

@Data
public class MessageRequest {
    private String text;
    private String chatId;
    private String senderId;
}