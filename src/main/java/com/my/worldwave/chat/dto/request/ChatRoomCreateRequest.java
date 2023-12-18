package com.my.worldwave.chat.dto.request;

import com.my.worldwave.chat.entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomCreateRequest {
    private String chatRoomName;

    @Builder
    public ChatRoomCreateRequest(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    public ChatRoom toEntity() {
        return ChatRoom.builder()
                .chatRoomName(chatRoomName)
                .build();
    }

}
