package com.zaki.grunttp.constant;

public final class Templates {

    public static final String STATUS_400 = "<!DOCTYPE html>\n" +
                                            "<html lang=\"en\">\n" +
                                            "<head>\n" +
                                            "    <meta charset=\"UTF-8\">\n" +
                                            "    <title>400 Bad Request</title>\n" +
                                            "</head>\n" +
                                            "<body>\n" +
                                            "    <h1>400 Bad Request</h1>\n" +
                                            "</body>\n" +
                                            "</html>";

    public static final String STATUS_401 = "<!DOCTYPE html>\n" +
                                            "<html lang=\"en\">\n" +
                                            "<head>\n" +
                                            "    <meta charset=\"UTF-8\">\n" +
                                            "    <title>401 Unauthorized</title>\n" +
                                            "</head>\n" +
                                            "<body>\n" +
                                            "    <h1>You are not authorized to access this resource!</h1>\n" +
                                            "</body>\n" +
                                            "</html>";

    public static final String STATUS_403 = "<!DOCTYPE html>\n" +
                                            "<html lang=\"en\">\n" +
                                            "<head>\n" +
                                            "    <meta charset=\"UTF-8\">\n" +
                                            "    <title>403 Forbidden</title>\n" +
                                            "</head>\n" +
                                            "<body>\n" +
                                            "    <h1>403 Forbidden</h1>\n" +
                                            "</body>\n" +
                                            "</html>";

    public static final String STATUS_404 = "<!DOCTYPE html>\n" +
                                            "<html lang=\"en\">\n" +
                                            "<head>\n" +
                                            "    <meta charset=\"UTF-8\">\n" +
                                            "    <title>404 Not Found</title>\n" +
                                            "</head>\n" +
                                            "<body>\n" +
                                            "    <h1>404 Not Found</h1>\n" +
                                            "</body>\n" +
                                            "</html>";

    public static final String STATUS_408 = "<!DOCTYPE html>\n"+
                                            "<html lang=\"en\">\n"+
                                            "<head>\n"+
                                            "    <meta charset=\"UTF-8\">\n"+
                                            "    <title>408 Request Timeout</title>\n"+
                                            "</head>\n"+
                                            "<body>\n"+
                                            "    <h1>408 Request Timeout</h1>\n"+
                                            "</body>\n"+
                                            "</html>";

    public static final String STATUS_413 = "<!DOCTYPE html>\n" +
                                            "<html lang=\"en\">\n" +
                                            "<head>\n" +
                                            "    <meta charset=\"UTF-8\">\n" +
                                            "    <title>413 Request Entity Too Large</title>\n" +
                                            "</head>\n" +
                                            "<body>\n" +
                                            "    <h1>413 Request Entity Too Large</h1>\n" +
                                            "</body>\n" +
                                            "</html>";

    public static final String STATUS_414 = "<!DOCTYPE html>\n" +
                                            "<html lang=\"en\">\n" +
                                            "<head>\n" +
                                            "    <meta charset=\"UTF-8\">\n" +
                                            "    <title>Request URI Too Long</title>\n" +
                                            "</head>\n" +
                                            "<body>\n" +
                                            "    <h1>Request URI Too Long</h1>\n" +
                                            "</body>\n" +
                                            "</html>";

    public static final String STATUS_500 = "<!DOCTYPE html>\n" +
                                            "<html lang=\"en\">\n" +
                                            "<head>\n" +
                                            "    <meta charset=\"UTF-8\">\n" +
                                            "    <title>500 Internal Server Error</title>\n" +
                                            "</head>\n" +
                                            "<body>\n" +
                                            "    <h1>500 Internal Server Error</h1>\n" +
                                            "    <p>#{error}</p>\n" +
                                            "</body>\n" +
                                            "</html>";

    public static final String STATUS_501 = "<!DOCTYPE html>\n" +
                                            "<html lang=\"en\">\n" +
                                            "<head>\n" +
                                            "    <meta charset=\"UTF-8\">\n" +
                                            "    <title>501 Method Not Implemented</title>\n" +
                                            "</head>\n" +
                                            "<body>\n" +
                                            "    <h1>501 Method Not Implemented</h1>\n" +
                                            "</body>\n" +
                                            "</html>";

    public static final String STATUS_505 = "<!DOCTYPE html>\n" +
                                            "<html lang=\"en\">\n" +
                                            "<head>\n" +
                                            "    <meta charset=\"UTF-8\">\n" +
                                            "    <title>505 HTTP Version Not Supported</title>\n" +
                                            "</head>\n" +
                                            "<body>\n" +
                                            "    <h1>505 HTTP Version Not Supported</h1>\n" +
                                            "</body>\n" +
                                            "</html>";
}
