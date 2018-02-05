package com.zaki.grunttp.writer;

import com.zaki.grunttp.config.log.ServerLogger;
import com.zaki.grunttp.constant.Global;
import com.zaki.grunttp.constant.StatusCode;
import com.zaki.grunttp.server.response.ServerResponse;

import java.io.*;
import java.util.List;
import java.util.Map;

import static com.zaki.grunttp.constant.Global.CARRIAGE_LINE;
import static com.zaki.grunttp.constant.Global.CARRIAGE_RETURN;

public class WriterImpl implements Writer {

    @Override
    public void writeBytes(OutputStream writer, ServerResponse resp, File respFile) {

        if (writer != null) {
            byte[] data = new byte[Global.BUFFER_SIZE]; // 4B
            try (
                    BufferedInputStream reader = new BufferedInputStream(new FileInputStream(respFile))
            ) {
                // write data
                writeHeader(writer, resp, respFile.length());
                writeClientData(writer, resp);
                int bytesRead;
                while ((bytesRead = reader.read(data, 0, Global.BUFFER_SIZE)) > 0) {
                    writer.write(data, 0, bytesRead);
                }
                writer.flush();
            } catch (IOException e) {
                ServerLogger.severe(e); // log only
            }
        }
    }

    @Override
    public void writeHtml(OutputStream writer, ServerResponse resp, String html) {

        if (writer != null) {
            if (html == null) {
                html = "";
            }
            try {
                writeHeader(writer, resp, html.length());
                writeClientData(writer, resp);
                writer.write(html.getBytes());
                writer.flush();
            } catch (IOException e) {
                ServerLogger.severe(e);
            }
        }
    }

    /**
     * Write response header stuff
     * @param out output stream
     * @param resp response
     * @param length content length
     * @throws IOException
     */
    private void writeHeader(final OutputStream out, final ServerResponse resp, long length) throws IOException {

        // get data
        StatusCode code = resp.getStatusCode();
        String version = "HTTP/1.1 " + code.getNumeric() + " " + code.getString() + CARRIAGE_LINE; // TODO httpVersion
        String contentType = "Content-Type: " + resp.getContentType() + CARRIAGE_LINE;
        String contentLength = "Content-Length: " + length;

        Map<String, Object> responseHeaders = resp.getHeaders();

        // write header properties
        out.write(version.getBytes());

        // write client properties
        for (Map.Entry header : responseHeaders.entrySet()) {
            out.write((header.getKey() + ": " + header.getValue() + CARRIAGE_LINE).getBytes());
        }

        out.write(contentType.getBytes());
        out.write(contentLength.getBytes());
        out.write(CARRIAGE_RETURN);
    }

    /**
     * Writes client written data to the response
     * @param out output stream
     * @param resp response
     * @throws IOException
     */
    private void writeClientData(final OutputStream out, final ServerResponse resp) throws IOException {

        List<String> clientWriter = resp.getLines();
        if (!clientWriter.isEmpty()) {
            for (String line : clientWriter) {
                out.write(line.getBytes());
            }
        }
    }
}
