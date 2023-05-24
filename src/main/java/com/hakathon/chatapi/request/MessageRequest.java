package com.hakathon.chatapi.request;

import lombok.Data;

@Data
public class MessageRequest {
    private String text;
    private String chatId;
    private String senderId;
}