package com.my.worldwave.notification.service;

import com.my.worldwave.notification.dto.NotificationMessage;
import com.my.worldwave.notification.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService implements MessageListener {

    private final EmitterRepository emitterRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String TOPIC = "notification:personal";
    private static final long SSE_SESSION_TIMEOUT = 30 * 60 * 1000L;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        NotificationMessage notificationMessage = (NotificationMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());
        if (notificationMessage != null && notificationMessage.getMemberId() != null) {
            sendNotification(Long.valueOf(notificationMessage.getMemberId()), notificationMessage.getMessage());
        }
    }

    public void publishNotification(NotificationMessage message) {
        redisTemplate.convertAndSend(TOPIC, message);
    }

    public SseEmitter createEmitter(Long memberId) {
        try {
            SseEmitter emitter = new SseEmitter(SSE_SESSION_TIMEOUT);
            emitterRepository.addEmitter(memberId, emitter);

            emitter.send(SseEmitter.event()
                    .id(String.valueOf(memberId))
                    .name("sse")
                    .data("connected"));

            emitter.onTimeout(() -> emitterRepository.removeEmitter(memberId, emitter));
            emitter.onCompletion(() -> emitterRepository.removeEmitter(memberId, emitter));
            return emitter;
        } catch (IOException e) {
            throw new RuntimeException("알림 서버와 연결에 실패했습니다.");
        }
    }

    public void sendNotification(Long memberId, String message) {
        SseEmitter emitter = emitterRepository.getEmitter(memberId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("notification").data(message));
            } catch (Exception e) {
                emitterRepository.removeEmitter(memberId, emitter);
                emitter.completeWithError(e);
            }
        }
    }

}
