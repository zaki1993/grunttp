package com.zaki.grunttp.constant;

public enum StatusCode {

	OK(200, "OK"),
	PERMANENT_REDIRECT(308, "Permanent Redirect"),
	BAD_REQUEST(400, "Bad Request"),
	UNAUTHORIZED(401, "Unauthorized"),
	FORBIDDEN(403, "Forbidden"),
	NOT_FOUND(404, "Not Found"),
	REQUEST_TIMEOUT(408, "Request Timeout"),
	REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),
	REQUEST_URI_TOO_LONG(414, "Request URI Too Long"),
	INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
	METHOD_NOT_IMPLEMENTED(501, "Method Not Implemented"),
	SERVICE_NOT_AVAILABLE(503, "Service Not Available"),
	HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported");

	StatusCode(int numeric, String msg) {
		this.numeric = numeric;
		this.msg = msg;
	}

	private String msg;
	private int numeric;

	/**
	 * @return the status code as integer
	 */
	public int getNumeric() {
		return this.numeric;
	}

	/**
	 * @return the status code as string
	 */
	public String getString() {
		return this.msg;
	}

	/**
	 * @param numeric status code in numeric
	 * @return status code
	 */
	public static StatusCode getCode(int numeric) {
		for (StatusCode code : StatusCode.values()) {
			if (code.numeric == numeric) {
				return code;
			}
		}
		return OK;
	}
}
