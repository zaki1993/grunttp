package com.zaki.grunttp.util.exceptions.runtime;

public class HandlerMethodNotFoundException extends RuntimeException {

    public HandlerMethodNotFoundException() {
        super();
    }

    public HandlerMethodNotFoundException(String msg) {
        super(msg);
    }

    public HandlerMethodNotFoundException(Throwable cause) {
        super(cause);
    }

    public HandlerMethodNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
