package com.hakathon.chatapi.model;

import lombok.Data;

@Data
public class MessageRequest {
    private String text;
    private long chatId;
    private String senderId;
}