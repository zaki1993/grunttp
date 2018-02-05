package com.zaki.grunttp.util.exceptions.runtime.response;

import com.zaki.grunttp.constant.StatusCode;

public class BadRequestException extends ServerResponseException {
    public BadRequestException() {
        super(StatusCode.BAD_REQUEST);
    }
}
