package com.zaki.grunttp.constant;

import com.zaki.grunttp.util.Utilizer;

public final class Global {
    public static final String ERR_PAGE_NOT_FOUND = "<h1>Error page not found..!</h1>";

    public static final String USER_DIR = System.getProperty("user.dir");

    public static final String FORWARD = "forward";

    public static final String REDIRECT = "redirect";

    public static final String HEADER_LOCATION = "Location";

    public static final String HEADER_HOST = "Host";

    public static final int BACK_LOG_SIZE = 127;

    public static final int SERVER_REQUEST_TIMEOUT_MILLISECONDS = 30_000;

    public static final int BUFFER_SIZE = Utilizer.getPerfectBlockSize();

    public static final int MAX_URI_LENGTH = 128; // 128 characters

    public static final int MAX_FILE_TRANSFER_SIZE = 1024 * 1024 * 21; // 21 MB

    public static final byte[] CARRIAGE_RETURN = Utilizer.getCarriageReturn();

    public static final String CARRIAGE_LINE = Utilizer.getCarriageLine();

    public static Environment ENVIRONMENT = Environment.DEV;
}
