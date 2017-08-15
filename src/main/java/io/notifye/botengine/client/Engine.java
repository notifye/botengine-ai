package io.notifye.botengine.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.notifye.botengine.client.bots.Bot;
import io.notifye.botengine.client.exception.BotException;
import io.notifye.botengine.client.interceptor.LoggingRequestInterceptor;
import io.notifye.botengine.client.model.Context;
import io.notifye.botengine.client.model.Entity;
import io.notifye.botengine.client.model.Interaction;
import io.notifye.botengine.client.model.Query;
import io.notifye.botengine.client.model.QueryResponse;
import io.notifye.botengine.client.model.ResponseInteraction;
import io.notifye.botengine.client.model.Story;

public final class Engine {
	private static final Logger log = LoggerFactory.getLogger(Engine.class);
	
	private static final String STORIES_RESOURCE = "%s/stories";
	private static final String STORY_RESOURCE = "%s/stories/%s";
	private static final String REFERENCE_RESOURCE = STORY_RESOURCE + "/contexts";
	private static final String INTERACTIONS_RESOURCE = STORY_RESOURCE + "/interactions";
	private static final String WELCOME_RESOURCE = INTERACTIONS_RESOURCE + "/welcome";
	private static final String INTERACTIONS_CTX_RESOURCE = INTERACTIONS_RESOURCE + "/%s";
	private static final String FALLBACK_RESOURCE = INTERACTIONS_RESOURCE + "/fallback";
	private static final String QUERY_RESOURCE = "%s/query";
	
	private static RestTemplate client;
	
	// Stories
	public static Story getStory(String id, Token token) throws BotException{
		Story story = null;
		String uri = getStoryUriResourceById(id);
		try{
			HttpHeaders headers = getDevHeaders(token);
			HttpEntity<String> entity = new HttpEntity<>(headers);
			
			ResponseEntity<String> response = getClient().exchange(uri, HttpMethod.GET, entity, String.class);
			
			if(response.getStatusCode().is2xxSuccessful()){
				log.debug("Get Story result -> {}", response);
				JsonNode node = getObjectMapper().readValue(response.getBody(), JsonNode.class);
				JsonNode idNode = node.get("id");
				JsonNode nameNode = node.get("name");
				JsonNode descriptionNode = node.get("description");
				story = Story.builder()
						.id(idNode.asText())
						.name(nameNode.asText())
						.description(descriptionNode.asText())
						.build();
			}
		}catch(Exception e){
			throw new BotException(e);
		}
		
		return story;
	}
	
	public static Story createStory(Story story, Token token) throws BotException{
		String uri = getRootStoryUriResource();
		log.info("Create Story with type -> {}", story);
		HttpHeaders headers = getDevHeaders(token);
		HttpEntity<Story> request = new HttpEntity<>(story, headers);

		ResponseEntity<String> response = getClient().exchange(uri, HttpMethod.POST, request, String.class);

		if(response.getStatusCode().is2xxSuccessful()){
			//get
			JsonNode node;
			try {
				String body = response.getBody();
				log.debug("Response body -> {}", body);
				node = getObjectMapper().readValue(body, JsonNode.class);
				JsonNode idNode = node.get("id");
				
				//TODO: Validate npe exception
				String id = idNode.asText();
				story.setId(id);
				log.debug("Create Story result -> {}", story);
			} catch (IOException e) {
				throw new BotException(e);
			}
		}
		return story;
	}
	
	public static void deleteStory(String id, Token token) {
		String uri = getStoryUriResourceById(id);
		log.debug("Delete Story by Id -> {}", id);
		HttpHeaders headers = getDevHeaders(token);
		HttpEntity<Story> request = new HttpEntity<>(headers);
		
		ResponseEntity<String> response = getClient().exchange(uri, HttpMethod.DELETE, request, String.class);
		if(response.getStatusCode().is2xxSuccessful()){
			//get
			log.debug("Removed Story result -> {}", response);
		}
	}
	
	//Interactions
	public static Interaction creatInteraction(Story story, Interaction interaction, Token token) throws BotException {
		switch (interaction.getType()) {
		case welcome:
			return createOrUpdateWelcomeMessage(story, interaction, token);
		case user:
			return createUserInteraction(story, interaction, token);
		case fallback:
			return createOrUpdateFalbackInteraction(story, interaction, token);
		default:
			return interaction;
		}
	}
	
	public static Interaction createUserInteraction(Story story, Interaction interaction, Token token) throws BotException{
		final String INTERACTION_URI_RESOURCE = String.format(INTERACTIONS_RESOURCE, Bot.API_URL, story.getId());
		HttpHeaders headers = getDevHeaders(token);
		HttpEntity<Interaction> request = new HttpEntity<>(interaction, headers);
		
		ResponseEntity<String> response = getClient().exchange(INTERACTION_URI_RESOURCE, HttpMethod.POST, request, String.class);
		log.debug("Interaction Response -> {}", response);
		if(response.getStatusCode().is2xxSuccessful()){
			log.debug("Interactions created suscessfull");
			JsonNode node;
			try {
				node = getObjectMapper().readValue(response.getBody(), JsonNode.class);
				JsonNode idNode = node.get("id");
				JsonNode nameNode = node.get("name");
				JsonNode descriptionNode = node.get("description");
				interaction.setId(idNode.asText());
			} catch (IOException e) {
				log.error("Error on get Id of interaction", e);
				if(log.isDebugEnabled()) {
					e.printStackTrace();
				}
				throw new BotException("Error on get Id of interaction", e);
			}
			
		}
		return interaction;
	}
	
	public static Interaction createOrUpdateWelcomeMessage(Story story, Interaction interaction, Token token){
		final String INTERACTION_URI_RESOURCE = String.format(WELCOME_RESOURCE, Bot.API_URL, story.getId());
		HttpHeaders headers = getDevHeaders(token);
		
		WelcomeMessageWrapper welcomeMessage = new WelcomeMessageWrapper(interaction.getResponses());
		
		HttpEntity<WelcomeMessageWrapper> request = new HttpEntity<>(welcomeMessage, headers);
		
		ResponseEntity<String> response = getClient().exchange(INTERACTION_URI_RESOURCE, HttpMethod.PUT, request, String.class);
		log.debug("Interaction Response -> {}", response);
		if(response.getStatusCode().is2xxSuccessful()){
			log.debug("Interactions created suscessfull");
		}
		return interaction;
	}
	
	public static Interaction createOrUpdateFalbackInteraction(Story story, Interaction interaction, Token token){
		final String INTERACTION_URI_RESOURCE = String.format(FALLBACK_RESOURCE, Bot.API_URL, story.getId());
		HttpHeaders headers = getDevHeaders(token);
		
		FallbackMessageWrapper welcomeMessage = new FallbackMessageWrapper(interaction.getResponses());
		
		HttpEntity<FallbackMessageWrapper> request = new HttpEntity<>(welcomeMessage, headers);
		
		ResponseEntity<String> response = getClient().exchange(INTERACTION_URI_RESOURCE, HttpMethod.PUT, request, String.class);
		log.debug("Interaction Response -> {}", response);
		if(response.getStatusCode().is2xxSuccessful()){
			log.debug("Interactions created suscessfull");
		}
		return interaction;
	}
	
	public static Interaction createChildInteraction(Story story, Interaction root, Interaction child, Token token) throws BotException{
		final String INTERACTION_CTX_URI_RESOURCE = String.format(INTERACTIONS_CTX_RESOURCE, Bot.API_URL, story.getId(), root.getId());
		HttpHeaders headers = getDevHeaders(token);
		
		initializeEmptyProperties(child);
		HttpEntity<Interaction> request = new HttpEntity<>(child, headers);
		
		ResponseEntity<String> response = getClient().exchange(INTERACTION_CTX_URI_RESOURCE, HttpMethod.POST, request, String.class);
		log.debug("Interaction Response -> {}", response);
		if(response.getStatusCode().is2xxSuccessful()){
			log.debug("Interactions created suscessfull");
			JsonNode node;
			try {
				node = getObjectMapper().readValue(response.getBody(), JsonNode.class);
				JsonNode idNode = node.get("id");
				JsonNode nameNode = node.get("name");
				JsonNode descriptionNode = node.get("description");
				root.getChilds().remove(child);
				child.setId(idNode.asText());
				root.getChilds().add(child);
				log.debug("Create Child Interaction suscessfully. \n\tRoot -> {}. \n\tChild -> {}", root, child);
			} catch (IOException e) {
				log.error("Error on get Id of interaction", e);
				if(log.isDebugEnabled()) {
					e.printStackTrace();
				}
				throw new BotException("Error on get Id of interaction", e);
			}
		}
		return root;
	}
	
	public static void createReference(Story story, List<Interaction> interactions, Token token, Context context) {
		final String REFERENCE_URI_RESOURCE = String.format(REFERENCE_RESOURCE, Bot.API_URL, story.getId());
		HttpHeaders headers = getDevHeaders(token);
		
		switch (context.getReferenceType()) {
		case BY_NAME:
			setContextIds(interactions, context);
			break;
		default:
			break;
		}
		
		HttpEntity<List<Context>> request = new HttpEntity<>(Arrays.asList(context), headers);
		ResponseEntity<String> response = getClient().exchange(REFERENCE_URI_RESOURCE, HttpMethod.PUT, request, String.class);
		log.debug("Referenced Response -> {}", response);
		if(response.getStatusCode().is2xxSuccessful()){
			log.debug("Referenced created suscessfull");
		}
	}
	
	/**
	 * @param interaction
	 */
	public static void initializeEmptyProperties(Interaction interaction) {
		//childs
		if(interaction.getChilds() == null) {
			interaction.setChilds(Collections.emptyList());
		}
		
		//parameters
		if(interaction.getParameters() == null) {
			interaction.setParameters(Collections.emptyList());
		}
		
		// entities
		if(interaction.getEntities() == null) {
			interaction.setEntities(Collections.emptyList());
		}
		
		//triggers
		if(interaction.getTriggers() == null) {
			interaction.setTriggers(Collections.emptyList());
		}
		
	}

	//Entities
	public static Entity createEntity(Entity entity, Token token){
		final String ENTITY_URI_RESOURCE = String.format("%s/entities", Bot.API_URL);
		log.debug("Post Entity with URI -> {}", ENTITY_URI_RESOURCE);
		
		HttpHeaders headers = getDevHeaders(token);
		HttpEntity<Entity> request = new HttpEntity<>(entity, headers);
		
		ResponseEntity<String> entityResponse = getClient().exchange(ENTITY_URI_RESOURCE, HttpMethod.POST, request, String.class);
		log.info("Entity response -> {}", entityResponse);
		if(entityResponse.getStatusCode().is2xxSuccessful()){
			log.info("Entity created suscessfull");
		}
		return entity;
	}
	
	//Query
	public static QueryResponse query(Story story, String query, Token token, String session){
		Query queryRequest = Query.builder()
				.query(query)
				.sessionId(session)
				.storyId(story.getId())
				.lifespan(getDefaultLifespan())
				.build();
		return q(queryRequest, token);
	}
	
	public static QueryResponse query(Query query, Token token, String session){
		if(Objects.isNull(query.getSessionId())){
			query.setSessionId(session);
		}
		return q(query, token);
	}
	public static QueryResponse query(Story story, Query query, Token token, String session){
		if(Objects.isNull(query.getStoryId())){
			query.setStoryId(story.getId());
		}
		return q(query, token);
	}
	
	private static int getDefaultLifespan() {
		return 2;
	}
	
	private static void setContextIds(List<Interaction> interactions, Context context) {
		String interactionNameIdIn = context.getId();
		List<String> outIds = context.getContextOut();
		log.debug("Ids -> {}", outIds.get(0));
		Optional<Interaction> in = interactions.stream().filter(root -> root.getName().equals(interactionNameIdIn)).findFirst();
		Optional<Interaction> out = interactions.stream().filter(child -> child.getName().equals(outIds.get(0))).findFirst();
		if(!out.isPresent()) {
			
			for (Interaction interaction : interactions) {
				log.debug("Trying get child for interaction {}", interaction);
				out = getChild(interaction, outIds.get(0));
				if(out.isPresent()) {
					break;
				}
			}
		}
		
		log.debug("Out interaction -> {}", out);
		context.setId(in.orElse(null).getId());
		context.setContextOut(Arrays.asList(out.get().getId()));
	}
	
	private static Optional<Interaction> getChild(Interaction interaction,  String name){
		List<Interaction> interactions = interaction.getChilds();
		if(interactions != null && interactions.size() > 0 && name != null && !name.isEmpty()) {
			log.debug("Get child interaction with name \n\t{} \n\tin interaction {}", name, interaction);
			return interactions.stream().filter(c -> c.getName().equals(name)).findFirst();
		}
		return Optional.empty();
	}
	
	public static String getSession(){
		long number = (long) Math.floor(Math.random() * 9_000_000_000L) + 1_000_000_000L;
		return String.valueOf(number);
	}
	
	private static String getRootStoryUriResource(){
		return String.format(STORIES_RESOURCE, Bot.API_URL);
	}
	
	private static String getStoryUriResourceById(String id){
		return String.format(STORY_RESOURCE, Bot.API_URL, id);
	}
	
	private static QueryResponse q(Query query, Token token){
		final String ENTITY_URI_RESOURCE = String.format(QUERY_RESOURCE, Bot.API_URL);
		HttpHeaders headers = getDevHeaders(token);
		HttpEntity<Query> request = new HttpEntity<>(query, headers);
		
		ResponseEntity<QueryResponse> entityResponse = getClient().exchange(ENTITY_URI_RESOURCE, HttpMethod.POST, request, QueryResponse.class);
		log.info("Entity response -> {}", entityResponse);
		
		QueryResponse response = null;
		if(entityResponse.getStatusCode().is2xxSuccessful()){
			log.info("Query executed suscessfull");
			response = entityResponse.getBody();
		}
		return response;
	}
	
	private static HttpHeaders getDevHeaders(Token token){
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "text/plain");
		headers.add("authorization", String.format("Bearer %s", token.getToken()));
		return headers;
	}
	
	private static RestTemplate getClient(){
		if(Objects.isNull(client)){
			client = new RestTemplate();
			List<HttpMessageConverter<?>> converters = new ArrayList<>();
			MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
			converters.add(new StringHttpMessageConverter());
			converters.add(converter);
			converter.setObjectMapper(getObjectMapper());
	
			client.setMessageConverters(converters);
			client.setInterceptors(getRequestInterceptors());
			return client;
		}
		return client;
	}
	
	private static List<ClientHttpRequestInterceptor> getRequestInterceptors(){
		LoggingRequestInterceptor loggingRequestInterceptor = new LoggingRequestInterceptor();

		List<ClientHttpRequestInterceptor> ris = new ArrayList<ClientHttpRequestInterceptor>();
		ris.add(loggingRequestInterceptor);
		return ris;
	}
	
	private static ObjectMapper getObjectMapper(){
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		return mapper;
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
