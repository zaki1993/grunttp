package com.zaki.grunttp.util.exceptions.runtime.response;

import com.zaki.grunttp.constant.StatusCode;

public class RequestTimeoutException extends ServerResponseException {
    public RequestTimeoutException() {
        super(StatusCode.REQUEST_TIMEOUT);
    }
}
