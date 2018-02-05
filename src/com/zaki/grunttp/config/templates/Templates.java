package com.zaki.grunttp.config.templates;

import com.zaki.grunttp.config.log.ServerLogger;
import com.zaki.grunttp.constant.StatusCode;

import java.util.Map;

import static com.zaki.grunttp.constant.Templates.*;

public final class Templates {

	private static Map<StatusCode, String> templates;

	/**
	 * Loads the response templates in map
	 */
	public static void loadTemplates() {
		ServerLogger.info("Loading templates..!");

		// initialize templates
		templates = Map.ofEntries(Map.entry(StatusCode.BAD_REQUEST, STATUS_400),
								  Map.entry(StatusCode.UNAUTHORIZED, STATUS_401),
								  Map.entry(StatusCode.FORBIDDEN, STATUS_403),
								  Map.entry(StatusCode.NOT_FOUND, STATUS_404),
								  Map.entry(StatusCode.REQUEST_TIMEOUT, STATUS_408),
								  Map.entry(StatusCode.REQUEST_ENTITY_TOO_LARGE, STATUS_413),
								  Map.entry(StatusCode.REQUEST_URI_TOO_LONG, STATUS_414),
								  Map.entry(StatusCode.INTERNAL_SERVER_ERROR,  STATUS_500),
								  Map.entry(StatusCode.METHOD_NOT_IMPLEMENTED, STATUS_501),
								  Map.entry(StatusCode.HTTP_VERSION_NOT_SUPPORTED, STATUS_505));
		ServerLogger.info(templates.size() + " templates loaded..!");
	}

	/**
	 * @param code status code
	 * @return response template by status code
	 */
	public static String getTemplateByStatusCode(StatusCode code) {
		return templates.get(code);
	}
}