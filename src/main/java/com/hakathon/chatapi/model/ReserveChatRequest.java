package com.hakathon.chatapi.model;

import lombok.Data;

@Data
public class ReserveChatRequest {
    private String chatId;
    private String managerId;
}