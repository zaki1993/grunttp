package com.zaki.grunttp.server.auth.controller;

import com.zaki.grunttp.constant.RequestMethod;
import com.zaki.grunttp.handler.anotations.Handler;
import com.zaki.grunttp.handler.anotations.RequestMapping;
import com.zaki.grunttp.server.ThreadContext;
import com.zaki.grunttp.server.request.ServerRequest;
import com.zaki.grunttp.server.response.ServerResponse;

/**
 * This class is just for testing purposes do not read
 */

@Handler(url="/login")
public class Login {

    @RequestMapping(method = RequestMethod.GET)
    public String doLoginGet(ServerRequest req, ServerResponse res) {

        ThreadContext tc = ThreadContext.getCurrent();
        System.out.println(tc.getRequest().getMethodType());
        return "redirect: login/login.html";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String doLoginPost(ServerRequest req, ServerResponse res) {

        return "redirect: login/logout.html";
    }
}
