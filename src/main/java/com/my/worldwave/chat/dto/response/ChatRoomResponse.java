package com.my.worldwave.chat.dto.response;

import com.my.worldwave.chat.entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomResponse {
    private Long chatRoomId;
    private String chatRoomName;

    @Builder
    public ChatRoomResponse(Long chatRoomId, String chatRoomName) {
        this.chatRoomId = chatRoomId;
        this.chatRoomName = chatRoomName;
    }

    public static ChatRoomResponse from(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getId())
                .chatRoomName(chatRoom.getChatRoomName())
                .build();
    }

}
