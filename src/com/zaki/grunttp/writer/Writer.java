package com.zaki.grunttp.writer;

import com.zaki.grunttp.server.response.ServerResponse;

import java.io.File;
import java.io.OutputStream;

public interface Writer {

    /**
     * Write respFile bytes to given output stream
     * @param writer output stream
     * @param resp response
     * @param respFile response file
     */
    void writeBytes(final OutputStream writer, final ServerResponse resp, final File respFile);

    /**
     * Writes raw string to given output stream
     * @param writer output stream
     * @param resp response
     * @param html string to write
     */
    void writeHtml(final OutputStream writer, final ServerResponse resp, String html);
}
