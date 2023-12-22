package com.my.worldwave.chat.controller;

import com.my.worldwave.chat.dto.request.ChatMessageRequest;
import com.my.worldwave.chat.dto.response.ChatMessageResponse;
import com.my.worldwave.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @MessageMapping("/chat/rooms/{roomId}")
    public void message(@DestinationVariable String roomId, ChatMessageRequest chatMessageRequest) {
        redisTemplate.convertAndSend("pub:chat", chatMessageRequest);
        chatMessageService.saveMessage(chatMessageRequest);
    }

    @GetMapping("/api/chat/rooms/{chatRoomId}/messages")
    public ResponseEntity<Page<ChatMessageResponse>> getPreviousMessages(@PathVariable String chatRoomId, Pageable pageable) {
        Page<ChatMessageResponse> messages = chatMessageService.getPreviousMessages(chatRoomId, pageable);
        return ResponseEntity.ok(messages);
    }

}
