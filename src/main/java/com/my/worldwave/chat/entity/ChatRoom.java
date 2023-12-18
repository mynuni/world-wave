package com.my.worldwave.chat.entity;

import com.my.worldwave.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;
    private String chatRoomName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member creator;

    @Builder
    public ChatRoom(String chatRoomName, Member creator) {
        this.chatRoomName = chatRoomName;
        this.creator = creator;
    }

}
