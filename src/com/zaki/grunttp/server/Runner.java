package com.zaki.grunttp.server;

import com.zaki.grunttp.util.exceptions.base.ServerBaseException;

public class Runner {
    public static void main(String[] args) throws ServerBaseException {
        Server httpServer = new HttpServerImpl(5557);
        httpServer.start();
    }
}
