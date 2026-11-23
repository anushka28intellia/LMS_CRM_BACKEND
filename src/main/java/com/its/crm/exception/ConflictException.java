package com.its.crm.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends CRMException {

    public ConflictException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
