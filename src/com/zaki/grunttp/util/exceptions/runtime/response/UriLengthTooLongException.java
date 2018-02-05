package com.zaki.grunttp.util.exceptions.runtime.response;

import com.zaki.grunttp.constant.StatusCode;

public class UriLengthTooLongException extends ServerResponseException {
    public UriLengthTooLongException() {
        super(StatusCode.REQUEST_URI_TOO_LONG);
    }
}
