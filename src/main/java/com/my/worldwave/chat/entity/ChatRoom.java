package com.my.worldwave.chat.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chatRoom")
public class ChatRoom {

    @Id
    private String chatRoomId;
    private String chatRoomName;
    private Long creatorId;
    private List<String> participantIds;

}
