package com.zaki.grunttp.util.exceptions.runtime.response;

import com.zaki.grunttp.constant.StatusCode;

public class UnauthorizedException extends ServerResponseException {
    public UnauthorizedException() {
        super(StatusCode.UNAUTHORIZED);
    }
}
