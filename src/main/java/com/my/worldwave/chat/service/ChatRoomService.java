package com.my.worldwave.chat.service;

import com.my.worldwave.chat.dto.request.ChatRoomCreateRequest;
import com.my.worldwave.chat.dto.response.ChatRoomResponse;
import com.my.worldwave.chat.entity.ChatRoom;
import com.my.worldwave.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getChatRooms() {
        return chatRoomRepository.findAll().stream()
                .map(ChatRoomResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createChatRoom(ChatRoomCreateRequest request) {
        ChatRoom chatRoom = chatRoomRepository.save(request.toEntity());
        chatRoomRepository.save(chatRoom);
        return chatRoom.getId();
    }

}
