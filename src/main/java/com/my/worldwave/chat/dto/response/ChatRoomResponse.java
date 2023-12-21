package com.my.worldwave.chat.dto.response;

import com.my.worldwave.chat.entity.ChatRoom2;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomResponse {
    private String chatRoomId;
    private String chatRoomName;
    private Long creatorId;
    private int participantCount;

    @Builder
    public ChatRoomResponse(String chatRoomId, String chatRoomName, Long creatorId, int participantCount) {
        this.chatRoomId = chatRoomId;
        this.chatRoomName = chatRoomName;
        this.creatorId = creatorId;
        this.participantCount = participantCount;
    }

    public static ChatRoomResponse from(ChatRoom2 chatRoom) {
        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .chatRoomName(chatRoom.getChatRoomName())
                .creatorId(chatRoom.getCreatorId())
                .participantCount(chatRoom.getParticipantIds().size())
                .build();
    }

}