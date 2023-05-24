package com.hakathon.chatapi.request;

import com.hakathon.chatapi.model.Manager;
import lombok.Data;

@Data
public class RedirectRequest {
    private String chatId;
    private Manager redirectManager;
}