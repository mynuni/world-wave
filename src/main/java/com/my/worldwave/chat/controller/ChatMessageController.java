package com.my.worldwave.chat.controller;

import com.my.worldwave.chat.dto.request.ChatMessageRequest;
import com.my.worldwave.chat.dto.response.ChatMessageResponse;
import com.my.worldwave.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final RedisTemplate<String, Object> redisTemplate;

    @MessageMapping("/chat/room/{roomId}")
    public void message(@DestinationVariable String roomId, ChatMessageRequest chatMessageRequest) {
        redisTemplate.convertAndSend("pub:chat", chatMessageRequest);
        chatMessageService.saveMessage(chatMessageRequest);
    }

    @GetMapping("/api/chat/rooms/{chatRoomId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getPreviousMessages(@PathVariable Long chatRoomId) {
        List<ChatMessageResponse> messages = chatMessageService.getPreviousMessages(chatRoomId);
        return ResponseEntity.ok(messages);
    }

}
