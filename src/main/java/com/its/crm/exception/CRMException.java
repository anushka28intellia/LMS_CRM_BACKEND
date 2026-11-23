package com.its.crm.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CRMException extends RuntimeException {

    private final HttpStatus status;

    public CRMException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public CRMException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
