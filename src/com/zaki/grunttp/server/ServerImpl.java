package com.zaki.grunttp.server;

import com.zaki.grunttp.config.Configuration;
import com.zaki.grunttp.config.log.ServerLogger;
import com.zaki.grunttp.config.templates.Templates;
import com.zaki.grunttp.constant.Environment;
import com.zaki.grunttp.constant.Global;
import com.zaki.grunttp.constant.ServerStatus;
import com.zaki.grunttp.memory.MemoryPool;
import com.zaki.grunttp.util.exceptions.base.ServerBaseException;
import com.zaki.grunttp.util.exceptions.runtime.ServerRuntimeException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.zaki.grunttp.constant.Environment.DEV;
import static com.zaki.grunttp.constant.Global.BACK_LOG_SIZE;
import static com.zaki.grunttp.constant.Global.SERVER_REQUEST_TIMEOUT_MILLISECONDS;

public abstract class ServerImpl implements Server {

    private static final long SERVER_SHUTDOWN_TIMEOUT = 10L;
    private ServerSocket server;
    private ExecutorService serverThreadPool;
    private boolean running;
    private final int port;
    protected volatile ServerStatus serverStatus;

    public ServerImpl(int port, Environment environment) throws ServerBaseException {
        this.port = port;
        this.running = false;
        Global.ENVIRONMENT = environment;
        init();
        logData();
        changeServerStatus(ServerStatus.READY);
    }

    public ServerImpl(int port) throws ServerBaseException {
        this(port, DEV);
    }

    @Override
    public void start() throws ServerBaseException {

        if (!running) {
            try {
                changeServerStatus(ServerStatus.STARTING);
                initServer();
                MemoryPool.onStart();
            } catch (IOException e) {
                changeServerStatus(ServerStatus.DOWN);
                throw new ServerBaseException(e.getMessage(), e);
            }
            ServerLogger.info("Server started running on port: " + this.port);
            this.running = true;
            run();
        }
    }

    @Override
    public void stop() {
        if (this.server != null && !this.server.isClosed()) {

            ServerLogger.info("Stopping server..!");
            changeServerStatus(ServerStatus.STOPPING);
            MemoryPool.onStop();
            this.running = false;
            try {
                this.server.close();
                serverThreadPool.shutdown();
                serverThreadPool.awaitTermination(SERVER_SHUTDOWN_TIMEOUT, TimeUnit.SECONDS);
            } catch (IOException | InterruptedException e) {
                throw new ServerRuntimeException(e);
            } finally {
                changeServerStatus(ServerStatus.DOWN);
            }
        }
    }

    /**
     * Binds the server socket to the port and sets the back log size
     * @throws IOException
     */
    private void initServer() throws IOException {

        this.server = new ServerSocket(this.port, BACK_LOG_SIZE);
        this.serverThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        //this.server.setOption(StandardSocketOptions.SO_KEEPALIVE, true);
    }

    /**
     * Initializes initial data such as configuration and templates
     * @throws ServerBaseException
     */
    private void init() throws ServerBaseException {

        Configuration.init();
        Templates.loadTemplates();
    }

    /**
     * The main method of the server which is used to accept new clients
     */
    private void run() {

        changeServerStatus(ServerStatus.ACTIVE);
        while (running) {
            try {
                Socket client = server.accept();
                client.setSoTimeout(SERVER_REQUEST_TIMEOUT_MILLISECONDS);
                client.setKeepAlive(true);
                serverThreadPool.submit(() -> acceptClient(client));
            } catch (IOException e) {
                ServerLogger.severe(e);
            } catch (ServerRuntimeException e) {
                ServerLogger.severe(e);
                if (e.getServerStatus() == ServerStatus.NOT_RECOVERABLE) {
                    ServerLogger.severe("Server status is: " + serverStatus);
                    stop();
                } else {
                    warn();
                }
            }
        }
    }

    /**
     * Logs warning
     */
    private void warn() {
        ServerLogger.warning("Server status is: " + serverStatus);
        ServerLogger.warning("Something unexpected happened..!");
        setServerStatus(ServerStatus.ACTIVE);
    }

    /**
     * Changes the server status and logs it
     * @param status server status
     */
    private void changeServerStatus(ServerStatus status) {
        this.serverStatus = status;
        ServerLogger.info("Server status changed to: " + serverStatus);
    }

    /**
     * Logging loaded data into the server
     */
    private void logData() {

        ServerLogger.info("Back log size: " + BACK_LOG_SIZE);
        ServerLogger.info("Server is ready to start..!");
    }
}