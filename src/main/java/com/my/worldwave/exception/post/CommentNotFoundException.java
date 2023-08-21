package com.my.worldwave.exception.post;

import com.my.worldwave.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {

    private static final String VALUE = "댓글";

    public CommentNotFoundException(Long id) {
        super(VALUE, id);
    }

}
