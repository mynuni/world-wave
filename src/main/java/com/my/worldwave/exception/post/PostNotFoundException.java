package com.my.worldwave.exception.post;

import com.my.worldwave.exception.NotFoundException;

public class PostNotFoundException extends NotFoundException {

    private static final String VALUE = "게시글";

    public PostNotFoundException(Long id) {
        super(VALUE, id);
    }

}
