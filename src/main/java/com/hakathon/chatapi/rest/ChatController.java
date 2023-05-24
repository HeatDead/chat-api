package com.hakathon.chatapi.rest;

import com.hakathon.chatapi.model.ChatEntity;
import com.hakathon.chatapi.model.ChatRequest;
import com.hakathon.chatapi.model.ChatStatus;
import com.hakathon.chatapi.model.ReserveChatRequest;
import com.hakathon.chatapi.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatRepository chatRepository;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    @PostMapping("/start")
    public ResponseEntity<Void> startChat(@RequestBody ChatRequest chatRequest) {
        log.info(chatRequest.toString());
        ChatEntity en = new ChatEntity();
        en.setClientId(chatRequest.getClientId());
        String data = "Обращение " + df.format(new Date());
        en.setChatName(data);
        log.info(data);
        en.setChatStatus(ChatStatus.OPENED);
        try {
           chatRepository.save(en);
        } catch (Exception e) {
            log.error(e.toString());
            return ResponseEntity.badRequest().build();
        }
        log.info(en.toString());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/userChats/{username}")
    public List<ChatEntity> getUserChats(@PathVariable String username) {
        return chatRepository.findAllByClientId(username);
    }

    @GetMapping("/availableChats/{username}")
    public List<ChatEntity> getAvailableChats(@PathVariable String username) {
        List<ChatEntity> chats = chatRepository.findAll();
        for (ChatEntity ce : chats) {
            if(ce.getManagerId() != null)
                if(ce.getManagerId() != username)
                    chats.remove(ce);
        }
        log.info("Available chats: " + chats.toString());
        return chats;
    }

    @PostMapping("/reserveChat")
    public void reserveChat(@RequestBody ReserveChatRequest request) {
        ChatEntity ce = chatRepository.findById(request.getChatId()).get();
        ce.setChatStatus(ChatStatus.ON_FIRST_LINE);
        ce.setManagerId(request.getManagerId());
        log.info("Reserved: " + ce.toString());
        chatRepository.save(ce);
    }
}