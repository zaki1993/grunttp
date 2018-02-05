package com.zaki.grunttp.config.exceptions;

public class DublicateUrlException extends InvalidConfigurationException {
    public DublicateUrlException(String url) {
        //There are two or more handlers for url: '
        super(new StringBuilder(60).append("There are two or more handlers for url '")
                                   .append(url)
                                   .append("'")
                                   .toString());
    }
}
