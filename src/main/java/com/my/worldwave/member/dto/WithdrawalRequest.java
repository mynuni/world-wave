package com.my.worldwave.member.dto;

import lombok.Getter;

@Getter
public class WithdrawalRequest {
    private String password;
    private boolean isAgreedToPolicy;

}
