package com.my.worldwave.chat.dto.response;

import com.my.worldwave.chat.entity.ChatMessage;
import com.my.worldwave.chat.entity.ChatMessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@Getter
@NoArgsConstructor
public class ChatMessageResponse {
    private String chatMessageId;
    private String chatRoomId;
    private String content;
    private Long senderId;
    private String senderNickname;
    private String senderProfileImage;
    private ChatMessageType chatMessageType;
    private LocalDateTime createdAt;

    @Builder
    public ChatMessageResponse(String chatMessageId, String chatRoomId, String content, Long senderId, String senderNickname, String senderProfileImage, ChatMessageType chatMessageType, LocalDateTime createdAt) {
        this.chatMessageId = chatMessageId;
        this.chatRoomId = chatRoomId;
        this.content = content;
        this.senderId = senderId;
        this.senderNickname = senderNickname;
        this.senderProfileImage = senderProfileImage;
        this.chatMessageType = chatMessageType;
        this.createdAt = createdAt;
    }

    public static ChatMessageResponse from(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .chatMessageId(chatMessage.getChatMessageId())
                .chatRoomId(chatMessage.getChatRoomId())
                .content(chatMessage.getContent())
                .senderId(chatMessage.getSenderId())
                .senderNickname(chatMessage.getSenderNickname())
                .senderProfileImage(chatMessage.getSenderProfileImage())
                .chatMessageType(chatMessage.getChatMessageType())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }

}