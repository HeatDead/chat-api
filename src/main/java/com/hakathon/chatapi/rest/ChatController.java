package com.hakathon.chatapi.rest;

import com.hakathon.chatapi.UserStorage;
import com.hakathon.chatapi.model.ChatEntity;
import com.hakathon.chatapi.model.ChatRequest;
import com.hakathon.chatapi.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatRepository chatRepository;
    @PostMapping("/start")
    public ResponseEntity<Void> register(@RequestBody ChatRequest chatRequest) {
        System.out.println(chatRequest);
        ChatEntity en = new ChatEntity();
        en.setClientId(chatRequest.getClientId());
        try {
           chatRepository.save(en);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/userChats/{username}")
    public List<ChatEntity> getUserChats(@PathVariable String username) {
        return chatRepository.findAllByClientId(username);
    }
}
