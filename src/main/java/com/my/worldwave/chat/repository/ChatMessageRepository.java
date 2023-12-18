package com.my.worldwave.chat.repository;

import com.my.worldwave.chat.dto.response.ChatMessageResponse;
import com.my.worldwave.chat.entity.ChatMessage;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query("SELECT new com.my.worldwave.chat.dto.response.ChatMessageResponse(" +
            "m.id, m.content, s.id, s.nickname, p.storedFileName, m.chatMessageType, m.createdAt) " +
            "FROM ChatMessage m " +
            "JOIN m.sender s " +
            "LEFT JOIN s.profileImage p " +
            "WHERE m.chatRoom.id = :chatRoomId " +
            "ORDER BY m.createdAt DESC ")
    List<ChatMessageResponse> findAllByChatRoomId(@Param("chatRoomId") Long chatRoomId);

}
