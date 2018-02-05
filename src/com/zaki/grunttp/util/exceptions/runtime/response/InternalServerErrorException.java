package com.zaki.grunttp.util.exceptions.runtime.response;

import com.zaki.grunttp.constant.StatusCode;

public class InternalServerErrorException extends ServerResponseException {

    public InternalServerErrorException(Throwable t) {
        super(StatusCode.INTERNAL_SERVER_ERROR, t);
    }

    public InternalServerErrorException() {
        super(StatusCode.INTERNAL_SERVER_ERROR);
    }
}
