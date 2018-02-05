package com.zaki.grunttp.config.log;

import com.zaki.grunttp.config.Configuration;
import com.zaki.grunttp.constant.Global;
import com.zaki.grunttp.util.Utilizer;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.*;

public final class ServerLogger {

	private static Logger LOGGER;
	static {
		LOGGER = Logger.getLogger(ServerLogger.class.getName());
		setProperties();
		info("Server logger instantiated..!");
	}

	private ServerLogger() { }

	/**
	 * Custom formatter for the logger
	 */
	private static class ServerFormatter extends Formatter {

	    public String format(LogRecord lr) {
	        return new StringBuilder(50).append(lr.getLevel())
									    .append(" :: ")
									    .append(new Date())
									    .append(" :: ")
									    .append(lr.getMessage())
									    .append(System.getProperty("line.separator"))
									    .toString();
	    }
	}

	/**
	 * Set some initial logger properties
	 */
	private static void setProperties() {

		ServerLogger.LOGGER.setUseParentHandlers(false);
        ConsoleHandler ch = new ConsoleHandler();
        ch.setFormatter(new ServerFormatter());
        ServerLogger.LOGGER.addHandler(ch);
        try {
            FileHandler fh = new FileHandler(Global.USER_DIR + File.separator + "server.log");
            fh.setFormatter(new ServerFormatter());
            ServerLogger.LOGGER.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ServerLogger.LOGGER.setLevel(Level.ALL);
	}

	/**
	 * Logs info
	 * @param msg string message
	 */
	public static void info(String msg) {
		LOGGER.info(msg);
	}

	/**
	 * Logs info
	 * @param e throwable message
	 */
	public static void info(Throwable e) {
		LOGGER.info(Utilizer.getStackTraceForLogging(e));
	}

	/**
	 * Logs severe
	 * @param msg string message
	 */
	public static void severe(String msg) {
		LOGGER.severe(msg);
	}

	/**
	 * Logs severe
	 * @param e throwable message
	 */
	public static void severe(Throwable e) {
		LOGGER.severe(Utilizer.getStackTraceForLogging(e));
	}

	/**
	 * Logs warning
	 * @param e throwable message
	 */
	public static void warning(Throwable e) {
		LOGGER.warning(Utilizer.getStackTraceForLogging(e));
	}

	/**
	 * Logs warning
	 * @param msg string message
	 */
	public static void warning(String msg) {
		LOGGER.severe(msg);
	}
}
