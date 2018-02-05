package com.zaki.grunttp.server.auth.controller;

import com.zaki.grunttp.constant.RequestMethod;
import com.zaki.grunttp.constant.StatusCode;
import com.zaki.grunttp.handler.anotations.Handler;
import com.zaki.grunttp.handler.anotations.RequestMapping;
import com.zaki.grunttp.server.request.ServerRequest;
import com.zaki.grunttp.server.response.ServerResponse;
import com.zaki.grunttp.util.exceptions.runtime.response.AccessDeniedForbiddenException;
import com.zaki.grunttp.util.exceptions.runtime.response.UnauthorizedException;

import java.util.Base64;

/**
 * This class is just for testing purposes do not read
 */

@Handler(url="/console")
public class Console {

    @RequestMapping(method= RequestMethod.GET)
    public String getConsole(ServerRequest req, ServerResponse res) {
        String authorized = req.getRequestProperty("Authorization");
        if (authorized != null) {
            byte[] decoded = Base64.getDecoder().decode(authorized.substring(authorized.indexOf(" ") + 1));
            String credetials = new String(decoded);
            int separatorIdx = credetials.indexOf(":");
            String username = credetials.substring(0, separatorIdx);
            String password = credetials.substring(separatorIdx + 1);
            if (username.equals("zaki") && password.equals("zaki")) {
                System.out.println(username + " " + password + " logged in!");
            } else {
                res.addHeader("Cache-Control", "no-cache");
                System.out.println("here");
                throw new AccessDeniedForbiddenException();
            }
        } else {
            res.setStatusCode(StatusCode.getCode(401));
            res.addHeader("WWW-Authenticate", "Basic realm=\"Grunttp server console. Authentication required!\"");
            res.addHeader("Cache-Control", "no-cache");
            System.out.println("heresds");
            throw new UnauthorizedException();
        }
        return "/login";
    }
}
