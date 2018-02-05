package com.zaki.grunttp.handler.excepions;

import com.zaki.grunttp.util.exceptions.base.ServerBaseException;

public class InvalidHandlerException extends ServerBaseException {
    public InvalidHandlerException(String s) {
        super(s);
    }

    public InvalidHandlerException(Throwable t) {
        super(t);
    }
}
