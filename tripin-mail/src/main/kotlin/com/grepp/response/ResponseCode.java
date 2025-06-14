package com.grepp.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    OK("2000", HttpStatus.OK,"success"),
    BAD_REQUEST("4000", HttpStatus.BAD_REQUEST,"bad request"),
    UNAUTHORIZED("4001", HttpStatus.UNAUTHORIZED, "unAuthorized"),
    INVALID_TOKEN("4002", HttpStatus.UNAUTHORIZED, "invalid token"),
    INTERNAL_SERVER_ERROR("5000", HttpStatus.INTERNAL_SERVER_ERROR, "server error");

    private final String code;
    private final HttpStatus status;
    private final String message;

}
