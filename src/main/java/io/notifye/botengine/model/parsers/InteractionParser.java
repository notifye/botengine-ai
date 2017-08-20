/**
 * 
 */
package io.notifye.botengine.model.parsers;

import org.springframework.http.ResponseEntity;

import io.notifye.botengine.exception.EntityParserException;
import io.notifye.botengine.model.Interaction;

/**
 * @author Adriano Santos
 *
 */
public class InteractionParser implements ResponseParser<Interaction> {

	/* (non-Javadoc)
	 * @see io.notifye.botengine.model.parsers.ResponseParser#parse(org.springframework.http.ResponseEntity)
	 */
	@Override
	public Interaction parse(ResponseEntity<String> response) throws EntityParserException {
		// TODO Auto-generated method stub
		return null;
	}

}
