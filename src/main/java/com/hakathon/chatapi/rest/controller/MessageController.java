package com.hakathon.chatapi.rest.controller;

import com.hakathon.chatapi.model.ChatEntity;
import com.hakathon.chatapi.model.MessageEntity;
import com.hakathon.chatapi.request.MessageRequest;
import com.hakathon.chatapi.repository.ChatRepository;
import com.hakathon.chatapi.repository.MessageRepository;
import com.hakathon.chatapi.repository.UserExtraRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {
    private final UserExtraRepository userExtraRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    @MessageMapping("/chat")
    public void sendMessage(@RequestBody MessageRequest messageReq) {
        ChatEntity ent = chatRepository.findById(messageReq.getChatId()).get();
        log.info(ent.toString());
        if(ent == null) return;
        log.info("senderId " + messageReq.getSenderId() + " ClientId " + ent.getClientId() + " ManagerId " + ent.getManagerId());
        //if(messageReq.getSenderId() != ent.getClientId() && messageReq.getSenderId() != ent.getManagerId()) return;
        MessageEntity en = new MessageEntity();
        en.setText(messageReq.getText());
        en.setChatId(messageReq.getChatId());
        en.setSenderId(messageReq.getSenderId());
        messageRepository.save(en);
        log.info(en.toString());
        simpMessagingTemplate.convertAndSend("/topic/messages/" + messageReq.getChatId(), messageReq);
    }

    @GetMapping("/messages/{chatId}")
    public List<MessageEntity> getMessages(@PathVariable String chatId) {
        log.info("Getting messages of chat: " + chatId);
        List<MessageEntity> ents = messageRepository.findAllByChatId(chatId);
        log.info("Messages of chat: " + ents);
        return ents;
    }
}