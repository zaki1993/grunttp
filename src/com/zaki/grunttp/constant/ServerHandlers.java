package com.zaki.grunttp.constant;

import java.util.Collections;
import java.util.List;

public final class ServerHandlers {

    private ServerHandlers() {}

    private static final List<String> serverHandlers = List.of("com.zaki.grunttp.server.auth.controller.Authenticator",
                                                               "com.zaki.grunttp.server.auth.controller.Console",
                                                               "com.zaki.grunttp.server.auth.controller.Login",
                                                               "com.zaki.grunttp.server.auth.controller.Uploader");
    /**
     * @return list of all defined handlers
     */
    public static List<String> getServerHandlers() {
        return Collections.unmodifiableList(serverHandlers);
    }
}
