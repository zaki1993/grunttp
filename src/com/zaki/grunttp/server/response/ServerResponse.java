package com.zaki.grunttp.server.response;

import com.zaki.grunttp.constant.StatusCode;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ServerResponse {

	/**
	 * @return response date
	 */
	Date getDate();

	/**
	 * @return response status code
	 */
	StatusCode getStatusCode();

	/**
	 * @return response content type
	 */
	String getContentType();

	/**
	 * @return response connection
	 */
	String getConnection();

	/**
	 * @return response headers
	 */
	Map<String, Object> getHeaders();

	/**
	 * Retrieves header value by property
	 * @param property header property
	 * @return header value if such exists
	 */
	Object getHeader(String property);

	/**
	 * Adds new header
	 * @param property header property
	 * @param value property value
	 */
	void addHeader(String property, Object value);

	/**
	 * Set response date
	 * @param date response date
	 */
	void setDate(Date date);

	/**
	 * Set response status code
	 * @param statusCode response status code
	 */
	void setStatusCode(StatusCode statusCode);

	/**
	 * Set response content type
	 * @param contentType content type
	 */
	void setContentType(String contentType);

	/**
	 * Set response connection
	 * @param connection response connection
	 */
	void setConnection(String connection);

	/**
	 * @return client external lines written to the response
	 */
    List getLines();

	/**
	 * Writes external line to the response
	 * @param line client line
	 * @param <T> any type that has .toString() method
	 */
	<T> void write(T line);
}
