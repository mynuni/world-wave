package com.my.worldwave.chat.controller;

import com.my.worldwave.chat.dto.request.ChatRoomCreateRequest;
import com.my.worldwave.chat.dto.response.ChatRoomResponse;
import com.my.worldwave.chat.dto.response.ParticipantResponse;
import com.my.worldwave.chat.service.ChatRoomService;
import com.my.worldwave.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getChatRooms() {
        List<ChatRoomResponse> chatRooms = chatRoomService.getChatRooms();
        return ResponseEntity.ok(chatRooms);
    }

    @GetMapping("/rooms/{chatRoomId}/participants")
    public ResponseEntity<List<ParticipantResponse>> getParticipants(@PathVariable String chatRoomId) {
        List<ParticipantResponse> participants = chatRoomService.getChatRoomParticipants(chatRoomId);
        return ResponseEntity.ok(participants);
    }

    @PostMapping("/rooms")
    public ResponseEntity<String> createChatRoom(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ChatRoomCreateRequest request) {
        String chatRoomId = chatRoomService.createChatRoom(userDetails.getMemberId(), request);
        return ResponseEntity.ok(chatRoomId);
    }

    @DeleteMapping("/rooms/{chatRoomId}")
    public ResponseEntity<String> leaveChatRoom(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable String chatRoomId) {
        chatRoomService.leaveChatRoom(chatRoomId, userDetails.getMemberId());
        return ResponseEntity.noContent().build();
    }

}
