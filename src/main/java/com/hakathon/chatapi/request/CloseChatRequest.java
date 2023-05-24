package com.hakathon.chatapi.request;

import lombok.Data;

@Data
public class CloseChatRequest {
    private String chatId;
    private String managerId;
}
