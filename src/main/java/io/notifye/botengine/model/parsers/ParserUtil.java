/**
 * 
 */
package io.notifye.botengine.model.parsers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Adriano Santos
 *
 */
public final class ParserUtil {
	
	public static ObjectMapper getJsonParser() {
		return jsonParser();
	}
	
	private static ObjectMapper jsonParser(){
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper;
	}

}
