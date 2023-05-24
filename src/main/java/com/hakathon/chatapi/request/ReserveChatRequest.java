package com.hakathon.chatapi.request;

import lombok.Data;

@Data
public class ReserveChatRequest {
    private String chatId;
    private String managerId;
}