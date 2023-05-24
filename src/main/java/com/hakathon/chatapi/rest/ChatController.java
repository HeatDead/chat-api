package com.hakathon.chatapi.rest;

import com.hakathon.chatapi.UserStorage;
import com.hakathon.chatapi.model.ChatEntity;
import com.hakathon.chatapi.model.ChatRequest;
import com.hakathon.chatapi.model.ReserveChatRequest;
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
            System.out.println(e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/userChats/{username}")
    public List<ChatEntity> getUserChats(@PathVariable String username) {
        return chatRepository.findAllByClientId(username);
    }

    @GetMapping("/avaibleChats/{username}")
    public List<ChatEntity> getAvaibleChats(@PathVariable String username) {
        List<ChatEntity> chats = chatRepository.findAll();
        for (ChatEntity ce : chats) {
            if(ce.getManagerId() != null)
                if(ce.getManagerId() != username)
                    chats.remove(ce);
        }
        return chats;
    }

    @PostMapping("/reserveChat")
    public void reserveChat(@RequestBody ReserveChatRequest request) {
        ChatEntity ce = chatRepository.findById(request.getChatId()).get();
        ce.setManagerId(request.getManagerId());
        chatRepository.save(ce);
    }
}
