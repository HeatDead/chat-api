package com.hakathon.chatapi.request;

import com.hakathon.chatapi.model.Manager;
import lombok.Data;

@Data
public class AvailableChatRequest {
    private String username;
    private Manager manager;
}
