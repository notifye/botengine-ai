package io.notifye.botengine;

import static io.notifye.botengine.BotResources.QUERY_RESOURCE;
import static io.notifye.botengine.BotResources.REFERENCE_RESOURCE;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import io.notifye.botengine.bots.Bot;
import io.notifye.botengine.exception.BotException;
import io.notifye.botengine.exception.QueryExecutionBotException;
import io.notifye.botengine.model.Context;
import io.notifye.botengine.model.Entity;
import io.notifye.botengine.model.Interaction;
import io.notifye.botengine.model.Query;
import io.notifye.botengine.model.QueryResponse;
import io.notifye.botengine.model.Story;
import io.notifye.botengine.model.parsers.ParserUtil;
import io.notifye.botengine.repository.EntityRepository;
import io.notifye.botengine.repository.InteractionRepository;
import io.notifye.botengine.repository.StoryRepository;
import io.notifye.botengine.repository.impl.EntityRepositoryImpl;
import io.notifye.botengine.repository.impl.InteractionRepositoryImpl;
import io.notifye.botengine.repository.impl.StoryRepositoryImpl;
import io.notifye.botengine.security.Token;
import io.notifye.botengine.util.HttpClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Engine {
	
	// Stories
	public static Story getStory(String id, Token token) throws BotException{
		StoryRepository storyRepository = new StoryRepositoryImpl(token);
		return storyRepository.getStory(id);
	}

	public static Story createStory(Story story, Token token) throws BotException{
		StoryRepository storyRepository = new StoryRepositoryImpl(token);
		return storyRepository.create(story);
	}

	public static void deleteStory(String id, Token token) throws BotException {
		StoryRepository storyRepository = new StoryRepositoryImpl(token);
		storyRepository.del(id);
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
		InteractionRepository interactionRepository = new InteractionRepositoryImpl(story, token);
		return interactionRepository.createUserInteraction(interaction);
	}

	public static Interaction createChildInteraction(Story story, Interaction root, Interaction child, Token token) throws BotException{
		InteractionRepository interactionRepository = new InteractionRepositoryImpl(story, token);
		initializeEmptyProperties(child);
		return interactionRepository.createChildInteraction(root, child);
	}

	public static Interaction createOrUpdateWelcomeMessage(Story story, Interaction interaction, Token token) throws BotException{
		InteractionRepository interactionRepository = new InteractionRepositoryImpl(story, token);
		return interactionRepository.createOrUpdateWelcomeMessage(interaction);
	}
	
	public static Interaction createOrUpdateFalbackInteraction(Story story, Interaction interaction, Token token) throws BotException{
		InteractionRepository interactionRepository = new InteractionRepositoryImpl(story, token);
		return interactionRepository.createOrUpdateFalbackInteraction(interaction);
	}
	
	public static void createReference(Story story, List<Interaction> interactions, Token token, Context context) {
		final String REFERENCE_URI_RESOURCE = String.format(REFERENCE_RESOURCE, Bot.API_URL, story.getId());
		HttpHeaders headers = HttpClient.getDevHeaders(token);
		
		switch (context.getReferenceType()) {
		case BY_NAME:
			setContextIds(interactions, context);
			break;
		default:
			break;
		}
		
		HttpEntity<List<Context>> request = new HttpEntity<>(Arrays.asList(context), headers);
		ResponseEntity<String> response = HttpClient.put(REFERENCE_URI_RESOURCE, request);
		log.debug("Referenced Response -> {}", response);
		if(HttpClient.isSuccessful(response)){
			log.debug("Referenced created suscessfull");
		}
	}
	
	/**
	 * @param interaction
	 */
	public static void initializeEmptyProperties(Interaction interaction) {
		//childs
		if(isNull(interaction.getChilds())) {
			interaction.setChilds(Collections.emptyList());
		}
		
		//parameters
		if(isNull(interaction.getParameters())) {
			interaction.setParameters(Collections.emptyList());
		}
		
		// entities
		if(isNull(interaction.getEntities())) {
			interaction.setEntities(Collections.emptyList());
		}
		
		//triggers
		if(isNull(interaction.getTriggers())) {
			interaction.setTriggers(Collections.emptyList());
		}
	}

	//Entities
	public static Entity createEntity(Entity entity, Token token) throws BotException{
		EntityRepository entityRepository = new EntityRepositoryImpl(token);
		return entityRepository.createEntity(entity);
	}
	
	//Query
	public static QueryResponse query(Story story, String query, Token token, String session) throws QueryExecutionBotException{
		Query queryRequest = Query.builder()
				.query(query)
				.sessionId(session)
				.storyId(story.getId())
				.lifespan(getDefaultLifespan())
				.build();
		return q(queryRequest, token);
	}
	
	public static QueryResponse query(Query query, Token token, String session) throws QueryExecutionBotException{
		if(isNull(query.getSessionId())){
			query.setSessionId(session);
		}
		return q(query, token);
	}
	public static QueryResponse query(Story story, Query query, Token token, String session) throws QueryExecutionBotException{
		if(isNull(query.getStoryId())){
			query.setStoryId(story.getId());
		}
		return q(query, token);
	}
	
	private static int getDefaultLifespan() {
		return Bot.DEFAULT_LIFESPAN;
	}
	
	private static double getDefaultConfidence() {
		return Bot.DEFAULT_CONFIDENCE;
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
	
	private static QueryResponse q(Query query, Token token) throws QueryExecutionBotException{
		final String ENTITY_URI_RESOURCE = String.format(QUERY_RESOURCE, Bot.API_URL);
		QueryResponse response = null;
		HttpHeaders headers = HttpClient.getDevHeaders(token);
		HttpEntity<Query> request = new HttpEntity<>(query, headers);
		
		if(isValidQuery(query)) {
			initializeQueryDefaults(query);
			
			try {
				ResponseEntity<String> entityResponse =  HttpClient.post(ENTITY_URI_RESOURCE, request);
				log.debug("Entity response -> {}", entityResponse);
				
				if(entityResponse.getStatusCode().is2xxSuccessful()){
					log.info("Query executed suscessfull");
					response = ParserUtil.getJsonParser().readValue(entityResponse.getBody(), QueryResponse.class);
				}
			} catch (IOException e) {
				log.error("Error on execute query", e.getMessage());
				if(log.isDebugEnabled()) {
					e.printStackTrace();
				}
			}
		}
		
		return response;
	}
	
	private static boolean isNull(Object obj) {
		return Objects.isNull(obj);
	}
	
	/**
	 * @param query
	 */
	private static void initializeQueryDefaults(Query query) {
		if(isNull(query.getConfidence()) || 0 <= query.getConfidence() ) {
			query.setConfidence(getDefaultConfidence());
		}
		
		if(isNull(query.getLifespan()) || 0 <= query.getLifespan()) {
			query.setLifespan(getDefaultLifespan());
		}
	}

	/**
	 * @param query
	 */
	private static boolean isValidQuery(Query query) throws QueryExecutionBotException {
		if(query.getSessionId().length() < 10) {
			throw new QueryExecutionBotException();
		}
		return true;
	}
	
}
