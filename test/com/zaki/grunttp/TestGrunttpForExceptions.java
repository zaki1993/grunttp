package com.zaki.grunttp;

import com.zaki.grunttp.config.exceptions.DublicateUrlException;
import com.zaki.grunttp.constant.Environment;
import com.zaki.grunttp.constant.ServerStatus;
import com.zaki.grunttp.server.HttpServerImpl;
import com.zaki.grunttp.server.Server;
import com.zaki.grunttp.util.exceptions.base.ServerBaseException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.net.Socket;

import static com.zaki.grunttp.constant.Environment.DEV;
import static com.zaki.grunttp.constant.Environment.TEST;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestGrunttpForExceptions {

    private static final int PORT = 5557;
    private static int currentPortOcc = 0;

    @Test
    public void test1ForDuplicateUrlException() throws ServerBaseException {

        assertThrows(DublicateUrlException.class,
                () -> new HttpServerImpl(PORT + (++currentPortOcc), DEV),
                "Duplicate handlers because server and user in this case is in the same root path");
    }

    @Test
    public void test2InitServer() throws ServerBaseException {

        Server localServer = new HttpServerImpl(PORT + (++currentPortOcc), TEST);
        assertTrue("Server status is READY", localServer.getServerStatus() == ServerStatus.READY);
    }

    @Test
    public void test3ServerStart() throws ServerBaseException {

        spawnNewServer(PORT + (++currentPortOcc), TEST);
    }

    @Test
    public void test4ServerStop() throws ServerBaseException {

        Server server = spawnNewServer(PORT + (++currentPortOcc), TEST);
        server.stop();
    }

    @Test
    public void test5ServerSetStatus() throws ServerBaseException {

        Server server = spawnNewServer(PORT + (++currentPortOcc), TEST);
        server.setServerStatus(ServerStatus.IDLE);
        assertTrue("Server status is IDLE", server.getServerStatus() == ServerStatus.IDLE);
    }

    @Test
    public void test6BadRequestInvalidRequest() throws ServerBaseException, IOException {

        spawnNewServer(PORT + (++currentPortOcc), TEST);
        Socket client = new Socket("localhost", PORT + currentPortOcc);
        client.getOutputStream().write("GET ASDASDA".getBytes());
        client.shutdownOutput();
        String response = new String(client.getInputStream().readAllBytes());
        client.close();
        assertTrue("Response is 400 Bad Request", response.startsWith("HTTP/1.1 400 Bad Request"));
    }

    @Test
    public void test7BadRequestHostNotProvidedForHttp11() throws ServerBaseException, IOException {

        spawnNewServer(PORT + (++currentPortOcc), TEST);
        Socket client = new Socket("localhost", PORT + currentPortOcc);
        client.getOutputStream().write("GET /login HTTP/1.1".getBytes());
        client.shutdownOutput();
        String response = new String(client.getInputStream().readAllBytes());
        client.close();
        assertTrue("Response is 400 Bad Request", response.startsWith("HTTP/1.1 400 Bad Request"));
    }

    @Test
    public void test8HttpVersionNotSupported() throws ServerBaseException, IOException {

        spawnNewServer(PORT + (++currentPortOcc), TEST);
        Socket client = new Socket("localhost", PORT + currentPortOcc);
        client.getOutputStream().write("GET /login HTTP/1.0".getBytes());
        client.shutdownOutput();
        String response = new String(client.getInputStream().readAllBytes());
        client.close();
        assertTrue("Response is 505 http version not supported", response.startsWith("HTTP/1.1 505 HTTP Version Not Supported"));
    }

    @Test
    public void test9RequestMethodNotImplemented() throws ServerBaseException, IOException {

        spawnNewServer(PORT + (++currentPortOcc), TEST);
        Socket client = new Socket("localhost", PORT + currentPortOcc);
        client.getOutputStream().write("CUSTOM_METHOD /login HTTP/1.1".getBytes());
        client.shutdownOutput();
        String response = new String(client.getInputStream().readAllBytes());
        client.close();
        assertTrue("Response is 501 Method not implemented", response.startsWith("HTTP/1.1 501 Method Not Implemented"));
    }

    @Test
    public void test9aNotFound404() throws IOException, ServerBaseException {
        spawnNewServer(PORT + (++currentPortOcc), TEST);
        Socket client = new Socket("localhost", PORT + currentPortOcc);
        client.getOutputStream().write("GET /asdasd HTTP/1.1\r\nHost: localhost\r\n\r\n".getBytes());
        client.shutdownOutput();
        String response = new String(client.getInputStream().readAllBytes());
        client.close();
        assertTrue("Response is 404 Not found", response.startsWith("HTTP/1.1 404 Not Found"));
    }

    @Test
    public void test9bPermanentRedirect() throws ServerBaseException, IOException {

        spawnNewServer(PORT + (++currentPortOcc), TEST);
        Socket client = new Socket("localhost", PORT + currentPortOcc);
        client.getOutputStream().write("GET /login HTTP/1.1\r\nHost: localhost\r\n\r\n".getBytes());
        client.shutdownOutput();
        String response = new String(client.getInputStream().readAllBytes());
        client.close();
        assertTrue("Response is 308 Permanent Redirect", response.startsWith("HTTP/1.1 308 Permanent Redirect"));
    }

    @Test
    public void test9cInternalServerError() throws ServerBaseException, IOException {

        spawnNewServer(PORT + (++currentPortOcc), TEST);
        Socket client = new Socket("localhost", PORT + currentPortOcc);
        client.getOutputStream().write("GET /auth HTTP/1.1\r\nHost: localhost\r\n\r\n".getBytes());
        client.shutdownOutput();
        String response = new String(client.getInputStream().readAllBytes());
        client.close();
        assertTrue("Response is 500 Internal server error", response.startsWith("HTTP/1.1 500 Internal Server Error"));
    }

    @Test
    public void test9d200OK() throws ServerBaseException, IOException {

        spawnNewServer(PORT + (++currentPortOcc), TEST);
        Socket client = new Socket("localhost", PORT + currentPortOcc);
        client.getOutputStream().write("GET /test HTTP/1.1\r\nHost: localhost\r\n\r\n".getBytes());
        client.shutdownOutput();
        String response = new String(client.getInputStream().readAllBytes());
        client.close();
        System.out.println(response);
        assertTrue("Response is 200 OK", response.startsWith("HTTP/1.1 200 OK"));
    }

    private Server spawnNewServer(int port, Environment env) throws ServerBaseException {
        Server server = new HttpServerImpl(port, env);
        new Thread(() -> {
            try {
                server.start();
            } catch (ServerBaseException e) {
                throw new RuntimeException(e);
            }
        }).start();
        return server;
    }
}
