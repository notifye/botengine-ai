/**
 * 
 */
package io.notifye.botengine.model.parsers;

import java.io.IOException;

import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;

import io.notifye.botengine.exception.EntityParserException;
import io.notifye.botengine.model.Story;

/**
 * @author Adriano Santos
 *
 */
public class StoryParser implements ResponseParser<Story>{

	/* (non-Javadoc)
	 * @see io.notifye.botengine.model.parsers.ResponseParser#parse(java.lang.String)
	 */
	@Override
	public Story parse(ResponseEntity<String> response) throws EntityParserException {
		try {
			return parseStory(response);
		} catch (IOException e) {
			throw new EntityParserException("Error on execute StoryParser", e);
		}
	}
	
	private static Story parseStory(ResponseEntity<String> response) throws IOException, JsonParseException, JsonMappingException {
		Story story;
		JsonNode node = ParserUtil.getJsonParser().readValue(response.getBody(), JsonNode.class);
		JsonNode idNode = node.get("id");
		JsonNode nameNode = node.get("name");
		JsonNode descriptionNode = node.get("description");
		story = Story.builder()
				.id(idNode.asText())
				.name(nameNode.asText())
				.description(descriptionNode.asText())
				.build();
		return story;
	}

}
