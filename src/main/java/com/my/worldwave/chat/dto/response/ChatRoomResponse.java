package com.my.worldwave.chat.dto.response;

import com.my.worldwave.chat.entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class ChatRoomResponse {
    private String chatRoomId;
    private String chatRoomName;
    private Long creatorId;
    private int participantCount;
    private List<String> participantIds;

    @Builder
    public ChatRoomResponse(String chatRoomId, String chatRoomName, Long creatorId, int participantCount, List<String> participantIds) {
        this.chatRoomId = chatRoomId;
        this.chatRoomName = chatRoomName;
        this.creatorId = creatorId;
        this.participantCount = participantCount;
        this.participantIds = participantIds;
    }

    public static ChatRoomResponse from(ChatRoom chatRoom) {
        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getChatRoomId())
                .chatRoomName(chatRoom.getChatRoomName())
                .creatorId(chatRoom.getCreatorId())
                .participantCount(chatRoom.getParticipants().size())
                .participantIds(chatRoom.getParticipants().stream()
                        .map(ChatRoom.Participant::getMemberId)
                        .collect(Collectors.toList()))
                .build();
    }

}
