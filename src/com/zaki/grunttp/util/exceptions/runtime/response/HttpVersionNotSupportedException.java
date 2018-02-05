package com.zaki.grunttp.util.exceptions.runtime.response;

import com.zaki.grunttp.constant.StatusCode;

public class HttpVersionNotSupportedException extends ServerResponseException {
    public HttpVersionNotSupportedException() {
        super(StatusCode.HTTP_VERSION_NOT_SUPPORTED);
    }
}
