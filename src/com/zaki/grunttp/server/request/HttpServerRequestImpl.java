package com.zaki.grunttp.server.request;

import com.zaki.grunttp.constant.Global;
import com.zaki.grunttp.constant.RequestMethod;
import com.zaki.grunttp.server.session.Session;
import com.zaki.grunttp.util.Utilizer;
import com.zaki.grunttp.util.exceptions.runtime.response.RequestTimeoutException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class HttpServerRequestImpl extends ServerRequestImpl {

    private static final int REQUEST_TIMEOUT_MILLISECONDS = 10_000; // 10 seconds

    private long startTime;
    private final RequestHeader requestHeader;
    private final RequestBody requestBody;

    public HttpServerRequestImpl(Session session) {
        super(session);
        requestHeader = createHeader();
        requestBody = createBody();
    }

    @Override
    public void readRequest(final InputStream input) throws IOException {

        this.startTime = System.currentTimeMillis();
        int bytesRead = readHeader(input);
        requestHeader.parseData(requestBody);
        readBody(bytesRead, input);
        if (requestBody.hasBytes()) {
            requestBody.parseData(requestHeader);
        }
    }

    @Override
    public byte[] getRequestBodyBytes() {
        return requestBody.getUploadedFile();
    }

    /**
     * Reads the request body bytes
     * @param currentBufferBytes the current position at the request body
     * @param input input stream that contains raw bytes
     * @throws IOException
     */
    private void readBody(int currentBufferBytes, InputStream input) throws IOException {

        boolean readBody = requestHeader.getMethod() == RequestMethod.POST &&
                           requestBody.hasBytes() && currentBufferBytes == Global.BUFFER_SIZE;
        byte[] buffer = new byte[Global.BUFFER_SIZE];
        if (readBody) {
            int bytesRead;
            while ((bytesRead = input.read(buffer, 0, Global.BUFFER_SIZE)) > 0) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - startTime >= REQUEST_TIMEOUT_MILLISECONDS) {
                    throw new RequestTimeoutException(); // TODO
                }
                requestBody.addBodyContent(Utilizer.toByteList(buffer));

                // If all the bytes from the stream are read we break the while loop
                // The reason for that is because the browser does not close the stream
                if (bytesRead != Global.BUFFER_SIZE) {
                    break;
                }
            }
        }
    }

    /**
     * Reads the request header
     * @param input input stream that contains raw bytes
     * @return the position of read body bytes
     * @throws IOException
     */
    private int readHeader(InputStream input) throws IOException {

        byte[] buffer = new byte[Global.BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = input.read(buffer, 0, Global.BUFFER_SIZE)) > 0) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - startTime >= REQUEST_TIMEOUT_MILLISECONDS) {
                throw new RequestTimeoutException();
            }
            int i = Utilizer.findCarriageReturnIndex(buffer);
            if (i != -1) {
                requestHeader.addHeaderContent(new String(Arrays.copyOfRange(buffer, 0, i)));
                if (bytesRead != i) {
                    requestBody.addBodyContent(Utilizer.toByteList(Arrays.copyOfRange(buffer, i, bytesRead)));
                }
                break;
            } else {
                requestHeader.addHeaderContent(new String(buffer));
            }
        }
        return bytesRead;
    }
}
