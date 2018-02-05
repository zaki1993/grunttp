package com.zaki.grunttp.server;

import com.zaki.grunttp.constant.ServerStatus;
import com.zaki.grunttp.util.exceptions.base.ServerBaseException;

import java.net.Socket;

public interface Server {

	/**
	 * Starts the server
	 * @throws ServerBaseException
	 */
	void start() throws ServerBaseException;

	/**
	 * Stops the server
	 */
	void stop();

	/**
	 * Accepts new tcp client
	 * @param client is tcp client
	 */
	void acceptClient(final Socket client);

    /**
     * @return server current status
     */
	ServerStatus getServerStatus();

    /**
     * Set the current server status
     * @param status server status
     */
	void setServerStatus(ServerStatus status);
}
