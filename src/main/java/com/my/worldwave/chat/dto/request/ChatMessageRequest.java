package com.my.worldwave.chat.dto.request;

import com.my.worldwave.chat.entity.ChatMessage;
import com.my.worldwave.chat.entity.ChatMessageType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatMessageRequest {
    private String chatRoomId;
    private String content;
    private Long senderId;
    private String senderNickname;
    private String senderProfileImage;
    private ChatMessageType chatMessageType;
    private final LocalDateTime createdAt = LocalDateTime.now();

    public ChatMessage toEntity() {
        return ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .content(content)
                .senderId(senderId)
                .senderNickname(senderNickname)
                .senderProfileImage(senderProfileImage)
                .chatMessageType(chatMessageType)
                .createdAt(createdAt)
                .build();
    }

}
