package com.my.worldwave.auth.util;

import lombok.Getter;

@Getter
public enum TokenType {
    ACCESS_TOKEN("accessToken"), REFRESH_TOKEN("refreshToken");

    private final String tokenName;

    TokenType(String tokenName) {
        this.tokenName = tokenName;
    }

}
