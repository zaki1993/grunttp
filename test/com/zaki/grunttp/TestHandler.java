package com.zaki.grunttp;

import com.zaki.grunttp.constant.RequestMethod;
import com.zaki.grunttp.handler.anotations.Handler;
import com.zaki.grunttp.handler.anotations.RequestMapping;
import com.zaki.grunttp.server.request.ServerRequest;
import com.zaki.grunttp.server.response.ServerResponse;

@Handler(url="/test")
public class TestHandler {

    @RequestMapping(method = RequestMethod.GET)
    public String getTest(ServerRequest req, ServerResponse res) {
        return "test.html";
    }
}
