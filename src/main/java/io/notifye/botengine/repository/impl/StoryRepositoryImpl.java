/**
 * 
 */
package io.notifye.botengine.repository.impl;

import static io.notifye.botengine.util.HttpClient.*;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;

import io.notifye.botengine.bots.Bot;
import io.notifye.botengine.exception.BotException;
import io.notifye.botengine.model.Story;
import io.notifye.botengine.model.parsers.ParserUtil;
import io.notifye.botengine.model.parsers.StoryParser;
import io.notifye.botengine.repository.StoryRepository;
import io.notifye.botengine.security.Token;
import io.notifye.botengine.util.HttpClient;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Adriano Santos
 *
 */
@Slf4j
public class StoryRepositoryImpl implements StoryRepository {
	private static final String STORIES_RESOURCE = "%s/stories";
	private static final String STORY_RESOURCE = "%s/stories/%s";
	
	private final Token token;
	private final StoryParser parser;
	
	public StoryRepositoryImpl(final Token token) {
		this(token, new StoryParser());
	}
	
	public StoryRepositoryImpl(final Token token, final StoryParser parser) {
		this.token = token;
		this.parser = parser;
	}
	
	@Override
	public Story getStory(String id) throws BotException {
		Story story = null;
		String uri = getStoryUriResourceById(id);
		try{
			HttpHeaders headers = getDevHeaders(token);
			HttpEntity<String> entity = new HttpEntity<>(headers);
			
			ResponseEntity<String> response = getClient().exchange(uri, HttpMethod.GET, entity, String.class);
			
			if(isSuccessful(response)){
				log.debug("Get Story result -> {}", response);
				story = parser.parse(response);
			}
		}catch(Exception e){
			throw new BotException(e);
		}
		
		return story;
	}
	
	@Override
	public Story create(Story story) throws BotException{
		log.info("Create Story with type -> {}", story);
		HttpHeaders headers = getDevHeaders(token);
		HttpEntity<Story> request = new HttpEntity<>(story, headers);
		ResponseEntity<String> response = post(getRootStoryUriResource(), request); 

		if(HttpClient.isSuccessful(response)){
			updateStoryEntity(story, response);
		}
		return story;
	}

	@Override
	public void del(String id) throws BotException {
		String uri = getStoryUriResourceById(id);
		log.debug("Delete Story by Id -> {}", id);
		HttpHeaders headers = HttpClient.getDevHeaders(token);
		HttpEntity<Story> request = new HttpEntity<>(headers);
		
		ResponseEntity<String> response = getClient().exchange(uri, HttpMethod.DELETE, request, String.class);
		if(HttpClient.isSuccessful(response)){
			log.debug("Removed Story result -> {}", response);
		}
	}
	
	/* (non-Javadoc)
	 * @see io.notifye.botengine.repository.StoryRepository#getStoryByName(java.lang.String)
	 */
	@Override
	public Story getStoryByName(String name) throws BotException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see io.notifye.botengine.repository.StoryRepository#getStories()
	 */
	@Override
	public List<Story> getStories() throws BotException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the token
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * @return the parser
	 */
	public StoryParser getParser() {
		return parser;
	}
	
	private static String getStoryUriResourceById(String id){
		return String.format(STORY_RESOURCE, Bot.API_URL, id);
	}
	
	private static String getRootStoryUriResource(){
		return String.format(STORIES_RESOURCE, Bot.API_URL);
	}
	
	private static void updateStoryEntity(Story story, ResponseEntity<String> response) throws BotException {
		JsonNode node;
		try {
			String body = response.getBody();
			log.debug("Response body -> {}", body);
			node = ParserUtil.getJsonParser().readValue(body, JsonNode.class);
			JsonNode idNode = node.get("id");
			
			//TODO: Validate npe exception
			String id = idNode.asText();
			if(id == null || id.isEmpty()) {
				throw new BotException("Error on create Story.");
			}
			story.setId(id);
			log.debug("Create Story result -> {}", story);
		} catch (IOException e) {
			throw new BotException(e);
		}
	}

}
