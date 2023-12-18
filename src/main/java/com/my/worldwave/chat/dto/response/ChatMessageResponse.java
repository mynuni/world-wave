package com.my.worldwave.chat.dto.response;

import com.my.worldwave.chat.entity.ChatMessage;
import com.my.worldwave.chat.entity.ChatMessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatMessageResponse {
    private Long chatMessageId;
    private String content;
    private Long senderId;
    private String senderNickname;
    private String senderProfileImage;
    private ChatMessageType chatMessageType;
    private LocalDateTime createdAt;

    @Builder
    public ChatMessageResponse(Long chatMessageId, String content, Long senderId, String senderNickname, String senderProfileImage, ChatMessageType chatMessageType, LocalDateTime createdAt) {
        this.chatMessageId = chatMessageId;
        this.content = content;
        this.senderId = senderId;
        this.senderNickname = senderNickname;
        this.senderProfileImage = senderProfileImage;
        this.chatMessageType = chatMessageType;
        this.createdAt = createdAt;
    }

    public static ChatMessageResponse from(ChatMessage chatMessage){
        return ChatMessageResponse.builder()
                .chatMessageId(chatMessage.getId())
                .content(chatMessage.getContent())
                .senderId(chatMessage.getSender().getId())
                .senderNickname(chatMessage.getSender().getNickname())
                .senderProfileImage(chatMessage.getSender().getProfileImage().getStoredFileName())
                .chatMessageType(chatMessage.getChatMessageType())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }

}