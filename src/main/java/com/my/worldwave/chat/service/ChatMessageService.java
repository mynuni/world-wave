package com.my.worldwave.chat.service;

import com.my.worldwave.chat.dto.request.ChatMessageRequest;
import com.my.worldwave.chat.dto.response.ChatMessageResponse;
import com.my.worldwave.chat.entity.ChatRoom;
import com.my.worldwave.chat.repository.ChatMessageRepository;
import com.my.worldwave.chat.repository.ChatRoomRepository;
import com.my.worldwave.member.entity.Member;
import com.my.worldwave.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendChatMessage(ChatMessageRequest message) {
        simpMessagingTemplate.convertAndSend("/topic/chat/room/" + message.getChatRoomId(), message);
    }

    @Transactional
    public void saveMessage(ChatMessageRequest chatMessageRequest) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageRequest.getChatRoomId()).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findById(chatMessageRequest.getSenderId()).orElseThrow(EntityNotFoundException::new);
        chatMessageRepository.save(chatMessageRequest.toEntity(chatRoom, member));
    }

    @Transactional(readOnly = true)
    public List<ChatMessageResponse> getPreviousMessages(Long chatRoomId) {
        return chatMessageRepository.findAllByChatRoomId(chatRoomId);
    }

}
