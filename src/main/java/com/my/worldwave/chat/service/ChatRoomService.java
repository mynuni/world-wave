package com.my.worldwave.chat.service;

import com.my.worldwave.chat.dto.request.ChatRoomCreateRequest;
import com.my.worldwave.chat.dto.response.ChatRoomResponse;
import com.my.worldwave.chat.dto.response.ParticipantResponse;
import com.my.worldwave.chat.entity.ChatRoom;
import com.my.worldwave.exception.chat.ChatRoomNotFoundException;
import com.my.worldwave.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final MongoTemplate mongoTemplate;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public List<ChatRoomResponse> getChatRooms() {
        List<ChatRoom> chatRooms = mongoTemplate.findAll(ChatRoom.class);
        return chatRooms.stream()
                .map(ChatRoomResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public String createChatRoom(Long memberId, ChatRoomCreateRequest request) {
        ChatRoom.Participant creator = ChatRoom.Participant.builder()
                .memberId(memberId.toString())
                .enteredAt(LocalDateTime.now())
                .build();

        ChatRoom chatRoom = ChatRoom.builder()
                .chatRoomName(request.getChatRoomName())
                .creatorId(memberId)
                .participants(new ArrayList<>(Arrays.asList(creator)))
                .build();

        ChatRoom save = mongoTemplate.save(chatRoom);
        return save.getChatRoomId();
    }

    @Transactional
    public void leaveChatRoom(String chatRoomId, Long memberId) {
        ChatRoom chatRoom = findChatRoomById(chatRoomId);

        if (chatRoom.getCreatorId().equals(memberId)) {
            mongoTemplate.remove(chatRoom);
        } else {
            List<ChatRoom.Participant> participants = chatRoom.getParticipants();
            participants.removeIf(participant -> participant.getMemberId().equals(memberId.toString()));
            mongoTemplate.save(chatRoom);
        }
    }

    @Transactional(readOnly = true)
    public List<ParticipantResponse> getChatRoomParticipants(String chatRoomId) {
        ChatRoom chatRoom = findChatRoomById(chatRoomId);

        List<Long> participantIds = chatRoom.getParticipants().stream()
                .map(participant -> Long.parseLong(participant.getMemberId()))
                .collect(Collectors.toList());

        return memberRepository.findParticipantsIn(participantIds).stream()
                .map(ParticipantResponse::from)
                .collect(Collectors.toList());
    }

    private ChatRoom findChatRoomById(String chatRoomId) {
        ChatRoom chatRoom = mongoTemplate.findById(chatRoomId, ChatRoom.class);
        if (chatRoom == null) {
            throw new ChatRoomNotFoundException(chatRoomId);
        }
        return chatRoom;
    }

}
