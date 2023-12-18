package com.my.worldwave.chat.controller;

import com.my.worldwave.chat.dto.request.ChatRoomCreateRequest;
import com.my.worldwave.chat.dto.response.ChatRoomResponse;
import com.my.worldwave.chat.service.ChatRoomService;
import com.my.worldwave.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api/chat/rooms")
@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping
    public ResponseEntity<List<ChatRoomResponse>> getChatRooms() {
        List<ChatRoomResponse> chatRooms = chatRoomService.getChatRooms();
        return ResponseEntity.ok(chatRooms);
    }

    @PostMapping
    public ResponseEntity<Long> createRoom(@RequestBody ChatRoomCreateRequest request) {
        Long chatRoomId = chatRoomService.createChatRoom(request);
        return ResponseEntity.ok(chatRoomId);
    }

}
