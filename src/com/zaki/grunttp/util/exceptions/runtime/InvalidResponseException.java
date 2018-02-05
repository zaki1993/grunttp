package com.zaki.grunttp.util.exceptions.runtime;

public class InvalidResponseException extends RuntimeException {

	public InvalidResponseException() {
        super();
    }

    public InvalidResponseException(String msg) {
        super(msg);
    }

    public InvalidResponseException(Throwable cause) {
        super(cause);
    }

    public InvalidResponseException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
