package com.my.worldwave.security;

import com.my.worldwave.exception.auth.AuthenticationFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.my.worldwave.auth.util.AuthenticationConstants.AUTHORIZATION_HEADER;
import static com.my.worldwave.auth.util.AuthenticationConstants.BEARER_PREFIX_LENGTH;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompInterceptor implements ChannelInterceptor {

    private final JwtTokenService jwtTokenService;

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
                log.info("INTERCEPTOR >> CONNECT");
                validateToken(stompHeaderAccessor);
                break;
            case SEND:
                // 메세지 발신 시
                break;
            case UNSUBSCRIBE:
                // 구독 해제 시
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

}
