package com.my.worldwave.chat.service;

import com.my.worldwave.chat.dto.request.ChatMessageRequest;
import com.my.worldwave.chat.dto.response.ChatMessageResponse;
import com.my.worldwave.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final MongoTemplate mongoTemplate;

    public void sendChatMessage(ChatMessageRequest message) {
        simpMessagingTemplate.convertAndSend("/topic/chat/rooms/" + message.getChatRoomId(), message);
    }

    @Transactional
    public void saveMessage(ChatMessageRequest chatMessageRequest) {
        mongoTemplate.insert(chatMessageRequest.toEntity());
    }

    @Transactional(readOnly = true)
    public Page<ChatMessageResponse> getPreviousMessages(String chatRoomId, Pageable pageable) {
        return chatMessageRepository.findByChatRoomIdOrderByCreatedAtDesc(chatRoomId, pageable)
                .map(ChatMessageResponse::from);
    }

}
