package com.zaki.grunttp.util.exceptions.runtime.response;

import com.zaki.grunttp.constant.StatusCode;

public class UrlNotHandledException extends ServerResponseException {
    public UrlNotHandledException() {
        super(StatusCode.NOT_FOUND);
    }
}
