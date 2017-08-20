/**
 * 
 */
package io.notifye.botengine.model.parsers;

import static io.notifye.botengine.model.parsers.ParserUtil.getJsonParser;

import java.io.IOException;

import org.springframework.http.ResponseEntity;

import io.notifye.botengine.exception.EntityParserException;
import io.notifye.botengine.model.QueryResponse;

/**
 * @author Adriano Santos
 *
 */
public class QueryResponseParser implements ResponseParser<QueryResponse> {

	/* (non-Javadoc)
	 * @see io.notifye.botengine.model.parsers.ResponseParser#parse(org.springframework.http.ResponseEntity)
	 */
	@Override
	public QueryResponse parse(ResponseEntity<String> response) throws EntityParserException {
		try {
			return getJsonParser().readValue(response.getBody(), QueryResponse.class);
		} catch (IOException e) {
			throw new EntityParserException("Error on parser query entity", e);
		}
	}

}
