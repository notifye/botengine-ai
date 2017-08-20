/**
 * 
 */
package io.notifye.botengine.repository.impl;

import static io.notifye.botengine.BotResources.FALLBACK_RESOURCE;
import static io.notifye.botengine.BotResources.INTERACTIONS_CTX_RESOURCE;
import static io.notifye.botengine.BotResources.INTERACTIONS_RESOURCE;
import static io.notifye.botengine.BotResources.WELCOME_RESOURCE;
import static io.notifye.botengine.util.HttpClient.getDevHeaders;
import static io.notifye.botengine.util.HttpClient.isSuccessful;
import static io.notifye.botengine.util.HttpClient.post;
import static io.notifye.botengine.util.HttpClient.put;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import io.notifye.botengine.bots.Bot;
import io.notifye.botengine.exception.BotException;
import io.notifye.botengine.model.Interaction;
import io.notifye.botengine.model.ResponseInteraction;
import io.notifye.botengine.model.Story;
import io.notifye.botengine.model.parsers.InteractionParser;
import io.notifye.botengine.model.parsers.ParserUtil;
import io.notifye.botengine.model.parsers.ResponseParser;
import io.notifye.botengine.repository.InteractionRepository;
import io.notifye.botengine.security.Token;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Adriano Santos
 *
 */
@Slf4j
public class InteractionRepositoryImpl implements InteractionRepository {
	
	private final Story story;
	private final Token token;
	private final HttpHeaders headers;
	private final ResponseParser<Interaction> parser;
	
	public InteractionRepositoryImpl(final Story story, final Token token) {
		this(story, token, new InteractionParser());
	}
	
	public InteractionRepositoryImpl(final Story story, final Token token, final ResponseParser<Interaction> parser) {
		this.story = story;
		this.token = token;
		this.parser = parser;
		this.headers = getDevHeaders(token);
	}

	/* (non-Javadoc)
	 * @see io.notifye.botengine.repository.InteractionRepository#createUserInteraction(io.notifye.botengine.model.Interaction)
	 */
	@Override
	public Interaction createUserInteraction(Interaction interaction) throws BotException {
		final String INTERACTION_URI_RESOURCE = String.format(INTERACTIONS_RESOURCE, Bot.API_URL, story.getId());
		HttpEntity<Interaction> request = new HttpEntity<>(interaction, headers);
		ResponseEntity<String> response = post(INTERACTION_URI_RESOURCE, request);
		
		log.debug("Interaction Response -> {}", response);
		if(isSuccessful(response)){
			log.debug("Interactions created suscessfull");
			updateInteractionEntity(interaction, response);
		}
		return interaction;
	}
	
	/* (non-Javadoc)
	 * @see io.notifye.botengine.repository.InteractionRepository#createChildInteraction(io.notifye.botengine.model.Interaction, io.notifye.botengine.model.Interaction)
	 */
	@Override
	public Interaction createChildInteraction(Interaction root, Interaction child) throws BotException {
		final String INTERACTION_CTX_URI_RESOURCE = String.format(INTERACTIONS_CTX_RESOURCE, Bot.API_URL, story.getId(), root.getId());
		HttpEntity<Interaction> request = new HttpEntity<>(child, headers);
		ResponseEntity<String> response = post(INTERACTION_CTX_URI_RESOURCE, request);

		log.debug("Interaction Response -> {}", response);
		if(isSuccessful(response)){
			log.debug("Interactions created suscessfull");
			updateChildInteractionEntity(root, child, response);
		}
		return root;
	}
	
	/* (non-Javadoc)
	 * @see io.notifye.botengine.repository.InteractionRepository#createOrUpdateWelcomeMessage(io.notifye.botengine.model.Interaction)
	 */
	@Override
	public Interaction createOrUpdateWelcomeMessage(Interaction welcomeInteraction) throws BotException {
		final String INTERACTION_URI_RESOURCE = String.format(WELCOME_RESOURCE, Bot.API_URL, story.getId());
		WelcomeMessageWrapper welcomeMessage = new WelcomeMessageWrapper(welcomeInteraction.getResponses());
		HttpEntity<WelcomeMessageWrapper> request = new HttpEntity<>(welcomeMessage, headers);
		
		ResponseEntity<String> response = put(INTERACTION_URI_RESOURCE, request);
		log.debug("Interaction Response -> {}", response);
		if(isSuccessful(response)){
			log.debug("Interactions created suscessfull");
		}
		return welcomeInteraction;
	}
	
	/* (non-Javadoc)
	 * @see io.notifye.botengine.repository.InteractionRepository#createOrUpdateFalbackInteraction(io.notifye.botengine.model.Interaction)
	 */
	@Override
	public Interaction createOrUpdateFalbackInteraction(Interaction interaction) throws BotException {
		final String INTERACTION_URI_RESOURCE = String.format(FALLBACK_RESOURCE, Bot.API_URL, story.getId());
		FallbackMessageWrapper welcomeMessage = new FallbackMessageWrapper(interaction.getResponses());
		HttpEntity<FallbackMessageWrapper> request = new HttpEntity<>(welcomeMessage, headers);
		
		ResponseEntity<String> response = put(INTERACTION_URI_RESOURCE, request);
		log.debug("Interaction Response -> {}", response);
		if(isSuccessful(response)){
			log.debug("Interactions created suscessfull");
		}
		return interaction;
	}

	public Story getStory() {
		return story;
	}

	public Token getToken() {
		return token;
	}

	public HttpHeaders getHeaders() {
		return headers;
	}

	public ResponseParser<Interaction> getParser() {
		return parser;
	}
	
	private static void updateInteractionEntity(Interaction interaction, ResponseEntity<String> response) throws BotException {
		JsonNode node;
		try {
			node = ParserUtil.getJsonParser().readValue(response.getBody(), JsonNode.class);
			JsonNode idNode = node.get("id");
			//JsonNode nameNode = node.get("name");
			//JsonNode descriptionNode = node.get("description");
			interaction.setId(idNode.asText());
		} catch (IOException e) {
			log.error("Error on get Id of interaction", e);
			if(log.isDebugEnabled()) {
				e.printStackTrace();
			}
			throw new BotException("Error on get Id of interaction", e);
		}
	}
	
	private static void updateChildInteractionEntity(Interaction root, Interaction child, ResponseEntity<String> response) throws BotException {
		try {
			updateInteractionEntity(child, response);
			root.getChilds().remove(child);
			root.getChilds().add(child);
			log.debug("Create Child Interaction suscessfully. \n\tRoot -> {}. \n\tChild -> {}", root, child);
		} catch (Exception e) {
			log.error("Error on get Id of interaction", e);
			if(log.isDebugEnabled()) {
				e.printStackTrace();
			}
			throw new BotException("Error on get Id of interaction", e);
		}
	}
	
	public static final class WelcomeMessageWrapper {
		
		@JsonProperty("responses")
		private List<ResponseInteraction> responses;
		
		@JsonIgnore
		private boolean disabled = false;
		
		public WelcomeMessageWrapper(final List<ResponseInteraction> responses){
			this.responses = responses;
		}
		
		public WelcomeMessageWrapper(final List<ResponseInteraction> responses, boolean disabled){
			this.responses = responses;
			this.disabled = disabled;
		}

		public List<ResponseInteraction> getResponses() {
			return responses;
		}

		public void setResponses(List<ResponseInteraction> responses) {
			this.responses = responses;
		}

		public boolean isDisabled() {
			return disabled;
		}

		public void setDisabled(boolean disabled) {
			this.disabled = disabled;
		}
		
	}
	
	public static final class FallbackMessageWrapper {
		
		@JsonProperty("responses")
		private List<ResponseInteraction> responses;
		
		@JsonProperty("disabled")
		private boolean disabled = false;
		
		public FallbackMessageWrapper(final List<ResponseInteraction> responses){
			this.responses = responses;
		}
		
		public FallbackMessageWrapper(final List<ResponseInteraction> responses, boolean disabled){
			this.responses = responses;
			this.disabled = disabled;
		}

		public List<ResponseInteraction> getResponses() {
			return responses;
		}

		public void setResponses(List<ResponseInteraction> responses) {
			this.responses = responses;
		}

		public boolean isDisabled() {
			return disabled;
		}

		public void setDisabled(boolean disabled) {
			this.disabled = disabled;
		}
	}

}
