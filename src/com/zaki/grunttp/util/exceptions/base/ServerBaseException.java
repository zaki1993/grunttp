package com.zaki.grunttp.util.exceptions.base;

public class ServerBaseException extends Exception {
    public ServerBaseException() {
        super();
    }

    public ServerBaseException(String msg) {
        super(msg);
    }

    public ServerBaseException(Throwable cause) {
        super(cause);
    }

    public ServerBaseException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
