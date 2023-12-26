package com.my.worldwave.exception.chat;

public class ChatRoomNotFoundException extends IllegalArgumentException {

    private static final String NOT_FOUND_MESSAGE_TEMPLATE = "존재하지 않는 채팅방입니다. ID:%s";

    public ChatRoomNotFoundException(String chatRoomId) {
        super(String.format(NOT_FOUND_MESSAGE_TEMPLATE, chatRoomId));
    }

}
