/**
 * 
 */
package io.notifye.botengine.model.parsers;

import org.springframework.http.ResponseEntity;

import io.notifye.botengine.exception.EntityParserException;
import io.notifye.botengine.model.Entity;

/**
 * @author Adriano Santos
 *
 */
public class EntityParser implements ResponseParser<Entity> {

	/* (non-Javadoc)
	 * @see io.notifye.botengine.model.parsers.ResponseParser#parse(org.springframework.http.ResponseEntity)
	 */
	@Override
	public Entity parse(ResponseEntity<String> response) throws EntityParserException {
		// TODO Auto-generated method stub
		return null;
	}

}
