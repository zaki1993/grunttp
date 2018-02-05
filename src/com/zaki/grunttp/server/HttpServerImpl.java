package com.zaki.grunttp.server;

import com.zaki.grunttp.config.Configuration;
import com.zaki.grunttp.config.log.ServerLogger;
import com.zaki.grunttp.config.templates.Templates;
import com.zaki.grunttp.constant.Environment;
import com.zaki.grunttp.constant.ServerStatus;
import com.zaki.grunttp.constant.StatusCode;
import com.zaki.grunttp.server.concurrent.ClientNotifier;
import com.zaki.grunttp.server.response.ServerResponse;
import com.zaki.grunttp.util.Utilizer;
import com.zaki.grunttp.util.exceptions.runtime.response.ServiceNotAvailable;
import com.zaki.grunttp.util.exceptions.base.ServerBaseException;
import com.zaki.grunttp.util.exceptions.runtime.response.ServerResponseException;
import com.zaki.grunttp.writer.Writer;
import com.zaki.grunttp.writer.WriterImpl;

import java.io.File;
import java.io.OutputStream;
import java.net.Socket;

import static com.zaki.grunttp.constant.Global.ERR_PAGE_NOT_FOUND;

public final class HttpServerImpl extends ServerImpl {

	public HttpServerImpl(int port, Environment environment) throws ServerBaseException {
		super(port, environment);
	}

	public HttpServerImpl(int port) throws ServerBaseException {
		super(port);
	}

	@Override
	public void start() throws ServerBaseException {
		super.start();
	}

	@Override
	public void stop() {
		super.stop();
	}

	@Override
	public ServerStatus getServerStatus() {
		return this.serverStatus;
	}

	@Override
	public void setServerStatus(ServerStatus status) {
		this.serverStatus = status;
	}

	@Override
	public void acceptClient(final Socket client) {

		ClientNotifier.notifyConnect();
		String clientHost = client.getInetAddress().getHostAddress();
		ServerLogger.info("Client connected: " + clientHost);

		Client httpClient = null;
		String msg = null;
		boolean hasError = true;
		try {
			if (getServerStatus() != ServerStatus.ACTIVE) {
				throw new ServiceNotAvailable();
			}
			httpClient = new HttpClientImpl(client);
			httpClient.performAction();
			hasError = false;
		} catch (ServerResponseException e) {
			msg = getTemplateByStatusCode(e.getStatusCode(), e.getTemplate(), Utilizer.displayErrorForWeb(e));
		} catch (Exception e) {
			StatusCode code = StatusCode.INTERNAL_SERVER_ERROR;
			msg = getTemplateByStatusCode(code, Templates.getTemplateByStatusCode(code), Utilizer.displayErrorForWeb(e));
		} finally {
			if (hasError) {
				sendError(httpClient, msg);
			}
			if (httpClient != null) {
				httpClient.closeConnection();
			}
			ClientNotifier.notifyDisconnect();
		}
	}

	/**
	 * Sends error to the client
	 * @param client http client
	 * @param msg message to be send
	 */
	private void sendError(final Client client, String msg) {

		if (client != null) {
			Configuration configuration = Configuration.getConfiguration();
			// write template or error page in case of error
			ServerResponse resp = client.getResponse();
			OutputStream out = client.getOutputStream();
			Writer writer = new WriterImpl();
			if (configuration.hasErrorPage()) {
				String errorPageDir = configuration.getErrorPage();
				File errorFile = new File(errorPageDir);
				if (errorFile.exists()) {
                    ((ClientImpl) client).setResponseContentType(errorFile);
                    ServerLogger.severe(msg);
                    writer.writeBytes(out, resp, errorFile);
                } else {
                    writer.writeHtml(out, resp, ERR_PAGE_NOT_FOUND);
                }
			} else {
				writer.writeHtml(out, resp, msg);
			}
		}
	}

	/**
	 * Returns template by given status code
	 * @param code status code
	 * @param template response template
	 * @param replacement template expression replacement
	 * @return formatted response template
	 */
	private String getTemplateByStatusCode(final StatusCode code, String template, String replacement) {

		String result;
		switch(code) {
			case INTERNAL_SERVER_ERROR:
				result = Utilizer.replaceTemplateVariable(template, "error", replacement);
				break;
			default:
				result = template;
				break;
		}
		return result;
	}
}
