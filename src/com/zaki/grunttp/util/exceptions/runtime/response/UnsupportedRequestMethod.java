package com.zaki.grunttp.util.exceptions.runtime.response;

import com.zaki.grunttp.constant.StatusCode;

public class UnsupportedRequestMethod extends ServerResponseException {
    public UnsupportedRequestMethod() {
        super(StatusCode.METHOD_NOT_IMPLEMENTED);
    }
}
