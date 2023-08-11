package com.my.worldwave.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

    private String exceptionClassName;
    private String message;

}
