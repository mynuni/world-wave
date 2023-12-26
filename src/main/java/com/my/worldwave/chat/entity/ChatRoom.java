package com.my.worldwave.chat.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chatRoom")
public class ChatRoom {

    @Id
    private String chatRoomId;
    private String chatRoomName;
    private Long creatorId;
    private List<Participant> participants = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Participant {
        private String memberId;
        private LocalDateTime enteredAt;
    }

}
