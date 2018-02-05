package com.zaki.grunttp.server.auth.controller;

import com.zaki.grunttp.constant.RequestMethod;
import com.zaki.grunttp.handler.anotations.Handler;
import com.zaki.grunttp.handler.anotations.RequestMapping;
import com.zaki.grunttp.server.ThreadContext;
import com.zaki.grunttp.server.request.ServerRequest;
import com.zaki.grunttp.server.response.ServerResponse;
import com.zaki.grunttp.server.session.Session;

/**
 * This class is just for testing purposes do not read
 */

@Handler(url="/auth")
public class Authenticator {

    @RequestMapping(method= RequestMethod.GET)
    public String auth(ServerRequest req, ServerResponse res) {
        boolean isAuthenticated = postAuthenticate();
        if (true) throw new NullPointerException();
        return "redirect : /login";
    }

    @RequestMapping(method= RequestMethod.POST)
    public String authenticate(ServerRequest req, ServerResponse res) {

        String username = req.getAttribute("username");
        String password = req.getAttribute("password");
        boolean isAuthenticated = preAuthenticate(username, password);
        if (!isAuthenticated) {
            return "server-login.html";
        }
        return "forward: /login";
    }

    private boolean postAuthenticate() {

        ThreadContext ctx = ThreadContext.getCurrent();
        Session session = ctx.getRequest().getSession();
        return session != null && session.isValid();
    }

    private boolean preAuthenticate(String username, String password) {
        return true;
        //return CredentialStore.lookup(username, password);
    }
}
