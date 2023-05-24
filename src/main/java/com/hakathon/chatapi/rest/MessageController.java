package com.hakathon.chatapi.rest;

import com.hakathon.chatapi.UserStorage;
import com.hakathon.chatapi.model.ChatEntity;
import com.hakathon.chatapi.model.MessageEntity;
import com.hakathon.chatapi.model.MessageModel;
import com.hakathon.chatapi.model.MessageRequest;
import com.hakathon.chatapi.repository.ChatRepository;
import com.hakathon.chatapi.repository.MessageRepository;
import com.hakathon.chatapi.repository.UserExtraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

        if(ent == null) return;

        if(messageReq.getSenderId() != ent.getClientId() && messageReq.getSenderId() != ent.getManagerId()) return;

        MessageEntity en = new MessageEntity();
        en.setText(messageReq.getText());
        en.setChatId(messageReq.getChatId());
        en.setSenderId(messageReq.getSenderId());
        messageRepository.save(en);
        simpMessagingTemplate.convertAndSend("/topic/messages/" + messageReq.getChatId(), messageReq);
    }

    @GetMapping("/messages/{chatId}")
    public List<MessageEntity> sendMessage(@PathVariable String chatId) {
        return messageRepository.findAllByChatId(chatId);
    }
}