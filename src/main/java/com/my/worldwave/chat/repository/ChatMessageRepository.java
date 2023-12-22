package com.my.worldwave.chat.repository;

import com.my.worldwave.chat.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, Long> {

    Page<ChatMessage> findByChatRoomIdOrderByCreatedAtDesc(String chatRoomId, Pageable pageable);

//    @Query("SELECT new com.my.worldwave.chat.dto.response.ChatMessageResponse(" +
//            "m.id, m.content, s.id, s.nickname, p.storedFileName, m.chatMessageType, m.createdAt) " +
//            "FROM ChatMessage m " +
//            "JOIN m.sender s " +
//            "LEFT JOIN s.profileImage p " +
//            "WHERE m.chatRoom.id = :chatRoomId " +
//            "ORDER BY m.createdAt DESC ")
//    Page<ChatMessageResponse> findAllByChatRoomId(@Param("chatRoomId") Long chatRoomId, Pageable pageable);

}
