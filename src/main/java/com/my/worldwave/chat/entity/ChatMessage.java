package com.my.worldwave.chat.entity;

import com.my.worldwave.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member sender;

    @Enumerated(EnumType.STRING)
    @Column(name = "chat_message_type")
    private ChatMessageType chatMessageType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public ChatMessage(String content, ChatRoom chatRoom, Member sender, ChatMessageType chatMessageType, LocalDateTime createdAt) {
        this.content = content;
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.chatMessageType = chatMessageType;
        this.createdAt = createdAt;
    }

}
