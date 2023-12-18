package com.my.worldwave.chat.dto.request;

import com.my.worldwave.chat.entity.ChatMessage;
import com.my.worldwave.chat.entity.ChatMessageType;
import com.my.worldwave.chat.entity.ChatRoom;
import com.my.worldwave.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatMessageRequest {
    private Long chatRoomId;
    private String content;
    private Long senderId;
    private String senderNickname;
    private String senderProfileImage;
    private ChatMessageType chatMessageType;
    private final LocalDateTime createdAt = LocalDateTime.now();

    public ChatMessage toEntity(ChatRoom chatRoom, Member sender) {
        return ChatMessage.builder()
                .content(content)
                .chatRoom(chatRoom)
                .sender(sender)
                .chatMessageType(chatMessageType)
                .build();
    }

}
