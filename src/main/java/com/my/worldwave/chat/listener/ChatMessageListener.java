package com.my.worldwave.chat.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.worldwave.chat.dto.request.ChatMessageRequest;
import com.my.worldwave.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatMessageListener implements MessageListener {

    private final ChatMessageService chatMessageService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ChatMessageRequest chatMessage = objectMapper.readValue(message.toString(), ChatMessageRequest.class);
            chatMessageService.sendChatMessage(chatMessage);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("올바르지 않은 형식의 메시지입니다.");
        }

    }

}
