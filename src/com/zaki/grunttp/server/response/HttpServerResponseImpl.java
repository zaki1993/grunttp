package com.zaki.grunttp.server.response;

import java.util.ArrayList;
import java.util.List;

public class HttpServerResponseImpl extends ServerResponseImpl {

    private List<String> out;

    public HttpServerResponseImpl() {
        super();
        this.out = new ArrayList<>();
        addSecurityHeaders();
    }

    @Override
    public List getLines() {
        return out;
    }

    @Override
    public <T> void write(T line) {
        out.add(line.toString());
    }

    /**
     * Add security headers to client response before client can add other headers.
     * The client is free to remove the security headers
     * TODO make optional in config file
     */
    private void addSecurityHeaders() {
        addHeader("content-security-policy", "default-src 'self' 'unsafe-inline' 'unsafe-eval' https: data:");
        addHeader("x-content-type-options", "nosniff");
        /*addHeader("public-key-pins", "pin-sha256=\"t/OMbKSZLWdYUDmhOyUzS+ptUbrdVgb6Tv2R+EMLxJM=\";" +
                                                    " pin-sha256=\"PvQGL6PvKOp6Nk3Y9B7npcpeL40twdPwZ4kA2IiixqA=\";" +
                                                    " pin-sha256=\"ZyZ2XrPkTuoiLk/BR5FseiIV/diN3eWnSewbAIUMcn8=\";" +
                                                    " pin-sha256=\"0kDINA/6eVxlkns5z2zWv2/vHhxGne/W0Sau/ypt3HY=\";" +
                                                    " pin-sha256=\"ktYQT9vxVN4834AQmuFcGlSysT1ZJAxg+8N1NkNG/N8=\";" +
                                                    " pin-sha256=\"rwsQi0+82AErp+MzGE7UliKxbmJ54lR/oPheQFZURy8=\";" +
                                                    " max-age=600; report-uri=\"localhost:5557\""); // TODO remove localhost */
        addHeader("strict-transport-security", "max-age=31536000; includeSubDomains; preload");
        addHeader("x-frame-options", "SAMEORIGIN");
        addHeader("x-xss-protection", "1; mode=block");
        addHeader("Referrer-Policy", "no-referrer");
    }

}
