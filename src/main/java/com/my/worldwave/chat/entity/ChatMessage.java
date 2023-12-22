package com.my.worldwave.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chatMessage")
public class ChatMessage {

    @Id
    private String chatMessageId;
    private String chatRoomId;
    private String content;
    private Long senderId;
    private String senderNickname;
    private String senderProfileImage;
    private ChatMessageType chatMessageType;
    private LocalDateTime createdAt;

}
