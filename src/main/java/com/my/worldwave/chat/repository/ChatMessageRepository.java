package com.my.worldwave.chat.repository;

import com.my.worldwave.chat.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, Long> {

    @Query(value = "{ 'chatRoomId': ?0, 'createdAt': { $gt: ?1 } }", sort = "{ 'createdAt' : -1 }")
    Page<ChatMessage> findChatMessages(String chatRoomId, LocalDateTime enteredAt, Pageable pageable);

}
