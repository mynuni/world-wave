package com.my.worldwave.security;

import com.my.worldwave.chat.entity.ChatRoom;
import com.my.worldwave.exception.auth.AuthenticationFailureException;
import com.my.worldwave.exception.chat.ChatRoomNotFoundException;
import com.my.worldwave.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.my.worldwave.auth.util.AuthenticationConstants.AUTHORIZATION_HEADER;
import static com.my.worldwave.auth.util.AuthenticationConstants.BEARER_PREFIX_LENGTH;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompInterceptor implements ChannelInterceptor {

    private static final Pattern pattern = Pattern.compile("/topic/chat/rooms/(.+)");
    private final JwtTokenService jwtTokenService;
    private final MongoTemplate mongoTemplate;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);
        StompCommand command = stompHeaderAccessor.getCommand();

        if (command != null) {
            handleCommand(stompHeaderAccessor, command);
        }

        return message;
    }

    /**
     * StompCommand에 따른 분기문
     * 최초 연결 시에 JWT를 검증하고, 이후 SEND 시에는 인증된 유저로 간주한다.
     */
    private void handleCommand(StompHeaderAccessor stompHeaderAccessor, StompCommand stompCommand) {
        switch (stompCommand) {
            case CONNECT:
                validateToken(stompHeaderAccessor);
                break;
            case SUBSCRIBE:
                processSubscribe(stompHeaderAccessor);
                break;
            default:
                break;
        }

    }

    private void validateToken(StompHeaderAccessor stompHeaderAccessor) {
        try {
            String authHeader = stompHeaderAccessor.getFirstNativeHeader(AUTHORIZATION_HEADER);
            if (authHeader == null) {
                throw new AuthenticationFailureException();
            }

            String accessToken = authHeader.substring(BEARER_PREFIX_LENGTH);
            jwtTokenService.isValidToken(accessToken);
        } catch (Exception e) {
            throw new AuthenticationFailureException();
        }
    }

    private void processSubscribe(StompHeaderAccessor stompHeaderAccessor) {
        try {
            Matcher matcher = pattern.matcher(Objects.requireNonNull(stompHeaderAccessor.getDestination()));
            if (matcher.find()) {
                String chatRoomId = matcher.group(1);
                String memberId = stompHeaderAccessor.getFirstNativeHeader("participant_id");
                addParticipant(chatRoomId, memberId);
            }
        } catch (NullPointerException e) {
            throw new RuntimeException("존재하지 않는 채팅방입니다.");
        }

    }

    private void addParticipant(String chatRoomId, String memberId) {
        ChatRoom chatRoom = mongoTemplate.findById(chatRoomId, ChatRoom.class);

        if (chatRoom == null) {
            throw new ChatRoomNotFoundException(chatRoomId);
        }

        if (!isParticipantExists(chatRoom, memberId)) {
            ChatRoom.Participant newParticipant = ChatRoom.Participant.builder()
                    .memberId(memberId)
                    .enteredAt(LocalDateTime.now())
                    .build();

            chatRoom.getParticipants().add(newParticipant);
            mongoTemplate.save(chatRoom);
        }
    }

    private boolean isParticipantExists(ChatRoom chatRoom, String memberId) {
        return chatRoom.getParticipants().stream()
                .anyMatch(participant -> participant.getMemberId().equals(memberId));
    }

}
