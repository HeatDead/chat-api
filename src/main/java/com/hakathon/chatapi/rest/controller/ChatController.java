package com.hakathon.chatapi.rest.controller;

import com.hakathon.chatapi.model.*;
import com.hakathon.chatapi.repository.ChatRepository;
import com.hakathon.chatapi.request.*;
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
        ChatEntity en = new ChatEntity();
        en.setClientId(chatRequest.getClientId());
        String data = "Обращение " + df.format(new Date());
        en.setChatName(data);
        en.setChatStatus(ChatStatus.PENDING_ON_FIRST_LINE);
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

    @GetMapping("/availableChats/{username}/{man}")
    public List<ChatEntity> getAvailableChats(@PathVariable String username, @PathVariable String man) {
        Manager manager = Manager.valueOf(man);
        log.info(username + " " + manager.toString());
        List<ChatEntity> chats = chatRepository.findAll();
        log.info(chats.toString());
        for (int i = 0; i < chats.size(); i++) {
            ChatEntity ce = chats.get(i);
            if (ce.getChatStatus() == ChatStatus.CLOSED) {
                chats.remove(ce);
                continue;
            }
            if(ce.getManagerId() != null) {
                if (!ce.getManagerId().equals(username)) {
                    chats.remove(ce);
                }
            } else {
                if(ce.getChatStatus() == ChatStatus.PENDING_ON_SECOND_LINE) {
                    if(ce.getSecondLineManager() != manager) {
                        chats.remove(ce);
                        continue;
                    }
                }
                if(ce.getChatStatus() == ChatStatus.PENDING_ON_FIRST_LINE) {
                    if(manager != Manager.CHAT_MANAGER) {
                        chats.remove(ce);
                    }
                }
            }
        }
        log.info("Available chats: " + chats.toString());
        return chats;
    }

    @GetMapping("/getChat/{chatId}")
    public ChatEntity getChat(@PathVariable String chatId) {
        ChatEntity ce = chatRepository.findById(chatId).get();
        return ce;
    }

    @PostMapping("/reserveChat")
    public void reserveChat(@RequestBody ReserveChatRequest request) {
        ChatEntity ce = chatRepository.findById(request.getChatId()).get();
        if(ce == null)
            throw new IllegalArgumentException("The chat with this id does not exist");
        if(ce.getManagerId() != null)
            throw new IllegalArgumentException("The chat already reserved");
        if(ce.getChatStatus() == ChatStatus.PENDING_ON_FIRST_LINE)
            ce.setChatStatus(ChatStatus.ON_FIRST_LINE);
        else if(ce.getChatStatus() == ChatStatus.PENDING_ON_SECOND_LINE)
            ce.setChatStatus(ChatStatus.ON_SECOND_LINE);
        ce.setManagerId(request.getManagerId());
        log.info("Reserved: " + ce.toString());
        chatRepository.save(ce);
    }

    @PostMapping("/redirectChat")
    public void redirectChat(@RequestBody RedirectRequest request) {
        ChatEntity ce = chatRepository.findById(request.getChatId()).get();
        if(ce == null)
            throw new IllegalArgumentException("The chat with this id does not exist");
        ce.setChatStatus(ChatStatus.PENDING_ON_SECOND_LINE);
        ce.setSecondLineManager(request.getRedirectManager());
        ce.setManagerId(null);
        log.info("Redirected: " + ce.toString());
        chatRepository.save(ce);
    }

    @PostMapping("/closeChat")
    public void closeChat(@RequestBody CloseChatRequest request) {
        ChatEntity ce = chatRepository.findById(request.getChatId()).get();
        if(ce == null)
            throw new IllegalArgumentException("The chat with this id does not exist");
        if(!ce.getManagerId().equals(request.getManagerId()))
            throw new IllegalArgumentException("The chat does not reserved by this manager");
        ce.setChatStatus(ChatStatus.CLOSED);
        ce.setManagerId(null);
        log.info("Redirected: " + ce.toString());
        chatRepository.save(ce);
    }
}