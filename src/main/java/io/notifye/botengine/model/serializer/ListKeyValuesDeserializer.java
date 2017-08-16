/**
 * 
 */
package io.notifye.botengine.model.serializer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author santosadriano
 *
 */
public class ListKeyValuesDeserializer extends JsonDeserializer<List<Map<String, String>>> {

	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.JsonDeserializer#deserialize(com.fasterxml.jackson.core.JsonParser, com.fasterxml.jackson.databind.DeserializationContext)
	 */
	@Override
	public List<Map<String, String>> deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, String>> list = mapper.readValue(parser, new TypeReference<List<Map<String, String>>>(){});
		return list;
	}

}
