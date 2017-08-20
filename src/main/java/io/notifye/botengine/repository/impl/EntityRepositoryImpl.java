/**
 * 
 */
package io.notifye.botengine.repository.impl;

import static io.notifye.botengine.BotResources.ENTITIES_RESOURCE;
import static io.notifye.botengine.util.HttpClient.getDevHeaders;
import static io.notifye.botengine.util.HttpClient.isSuccessful;
import static io.notifye.botengine.util.HttpClient.post;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import io.notifye.botengine.bots.Bot;
import io.notifye.botengine.exception.BotException;
import io.notifye.botengine.model.Entity;
import io.notifye.botengine.model.parsers.EntityParser;
import io.notifye.botengine.model.parsers.ResponseParser;
import io.notifye.botengine.repository.EntityRepository;
import io.notifye.botengine.security.Token;
import lombok.extern.slf4j.Slf4j;

/**
 * @author santosadriano
 *
 */
@Slf4j
public class EntityRepositoryImpl implements EntityRepository {
	
	private final Token token;
	private final HttpHeaders headers;
	private final ResponseParser<Entity> parser;

	public EntityRepositoryImpl(final Token token) {
		this(token, new EntityParser());
	}
	
	public EntityRepositoryImpl(final Token token, final ResponseParser<Entity> parser) {
		this.token = token;
		this.parser = parser;
		this.headers = getDevHeaders(token);
	}

	/* (non-Javadoc)
	 * @see io.notifye.botengine.repository.EntityRepository#createEntity(io.notifye.botengine.model.Entity)
	 */
	@Override
	public Entity createEntity(Entity entity) throws BotException {
		final String ENTITY_URI_RESOURCE = String.format(ENTITIES_RESOURCE, Bot.API_URL);
		log.debug("Post Entity with URI -> {}", ENTITY_URI_RESOURCE);
		
		HttpEntity<Entity> request = new HttpEntity<>(entity, headers);
		ResponseEntity<String> entityResponse = post(ENTITY_URI_RESOURCE, request);

		log.info("Entity response -> {}", entityResponse);
		if(isSuccessful(entityResponse)){
			log.info("Entity created suscessfull");
		}
		return entity;
	}

	public Token getToken() {
		return token;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public ResponseParser<Entity> getParser() {
		return parser;
	}

}
