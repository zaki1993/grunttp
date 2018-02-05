package com.zaki.grunttp.util.exceptions.runtime.response;

import com.zaki.grunttp.constant.StatusCode;

public class CallHandlerException extends ServerResponseException {
    public CallHandlerException(Throwable t) {
        super(StatusCode.INTERNAL_SERVER_ERROR, t);
    }
}
