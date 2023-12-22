package com.my.worldwave.chat.service;

import com.my.worldwave.chat.dto.request.ChatRoomCreateRequest;
import com.my.worldwave.chat.dto.response.ChatRoomResponse;
import com.my.worldwave.chat.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final MongoTemplate mongoTemplate;

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getChatRooms() {
        List<ChatRoom> all = mongoTemplate.findAll(ChatRoom.class);
        return all.stream()
                .map(ChatRoomResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public String createChatRoom(Long memberId, ChatRoomCreateRequest request) {
        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomName(request.getChatRoomName())
                .creatorId(memberId)
                .participantIds(new ArrayList<>(Arrays.asList(memberId.toString())))
                .build();

        ChatRoom save = mongoTemplate.save(chatRoom);
        return save.getChatRoomId();
    }

}
