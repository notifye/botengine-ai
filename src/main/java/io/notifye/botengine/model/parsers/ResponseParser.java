/**
 * 
 */
package io.notifye.botengine.model.parsers;

import org.springframework.http.ResponseEntity;

import io.notifye.botengine.exception.EntityParserException;

/**
 * @author Adriano Santos
 *
 */
public interface ResponseParser<T> {
	
	/**
	 * @param response
	 * @return
	 * @throws EntityParserException
	 */
	public T parse(ResponseEntity<String> response) throws EntityParserException;

}
