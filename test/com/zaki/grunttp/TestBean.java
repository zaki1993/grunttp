package com.zaki.grunttp;

import com.zaki.grunttp.handler.anotations.WebConfig;

@WebConfig
public class TestBean {

    public String getStaticLocation() {
        return "/static";
    }

    public String getErrorPage() {
        return null;
    }
}
