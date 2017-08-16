package io.notifye.botengine.client.action.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import io.notifye.botengine.client.Engine;
import io.notifye.botengine.client.Token;
import io.notifye.botengine.client.action.InteractionAction;
import io.notifye.botengine.client.bots.Bot;
import io.notifye.botengine.client.exception.BotException;
import io.notifye.botengine.client.model.Context;
import io.notifye.botengine.client.model.Context.ReferenceType;
import io.notifye.botengine.client.model.Interaction;
import io.notifye.botengine.client.model.enums.InteractionType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public final @Data class InteractionsActionController implements InteractionAction {
	
	private static final long serialVersionUID = 1L;
	
	private final String ACTION_NAME = InteractionAction.class.getSimpleName();
	
	private final Bot bot;
	private final Token token;
	private List<Interaction> interactions;
	private List<Context> contexts = new CopyOnWriteArrayList<Context>(new ArrayList<Context>());
	
	public List<Interaction> getInteractions(){
		return this.interactions;
	}

	@Override
	public InteractionAction add() {
		this.interactions = new CopyOnWriteArrayList<Interaction>(new ArrayList<Interaction>());
		return this;
	}

	@Override
	public InteractionAction welcome(Interaction welcomeInteraction) {
		Objects.requireNonNull(this.interactions, "Please necessary add interactions");
		Objects.requireNonNull(welcomeInteraction, "Interaction is Mandatory");

		welcomeInteraction.setType(InteractionType.welcome);
		this.interactions.add(welcomeInteraction);
		return this;
	}

	@Override
	public InteractionAction fallback(Interaction fallbackInteraction) {
		Objects.requireNonNull(this.interactions, "Please necessary add interactions");
		Objects.requireNonNull(fallbackInteraction, "Interaction is Mandatory");

		fallbackInteraction.setType(InteractionType.fallback);
		this.interactions.add(fallbackInteraction);
		return this;
	}

	@Override
	public InteractionAction interaction(Interaction userInteraction) {
		Objects.requireNonNull(this.interactions, "Please necessary add interactions");
		Objects.requireNonNull(userInteraction, "Interaction is Mandatory");
		
		userInteraction.setType(InteractionType.user);
		this.interactions.add(userInteraction);
		return this;
	}
	
	@Override
	public InteractionAction referenceById(String interactionIdIn, String interactionIdOut) {
		// Create context
		return addContext(ReferenceType.BY_ID, interactionIdIn, interactionIdOut, true);
	}

	@Override
	public InteractionAction referenceById(String interactionIdIn, String interactionIdOut, boolean root) {
		// Create context
		return addContext(ReferenceType.BY_ID, interactionIdIn, interactionIdOut, root);
	}

	@Override
	public InteractionAction referenceByName(String interactionNameIn, String interactionNameOut) {
		return addContext(ReferenceType.BY_NAME, interactionNameIn, interactionNameOut, true);
	}

	@Override
	public InteractionAction referenceByName(String interactionNameIn, String interactionNameOut, boolean root) {
		return addContext(ReferenceType.BY_NAME, interactionNameIn, interactionNameOut, root);
	}
	
	@Override
	public Bot build() {
		Objects.requireNonNull(this.interactions, "Please necessary add interactions");
		log.info("Compiling Interactions");
		createInteractions();
		createReferences();
		return this.bot;
	}

	@Override
	public String getActionName() {
		return ACTION_NAME;
	}

	@Override
	public int getOrderExecution() {
		return 0;
	}

	public Token getToken() {
		return token;
	}
	
	private void createInteractions() {
		this.interactions.stream()
			.forEach(interaction -> {
				
				Engine.initializeEmptyProperties(interaction);
				
				log.info("Creating Entities...");
				createEntities(interaction);
				
				//CREATE INTERACTION
				log.debug("Creating Interaction -> {}", interaction);
				try {
					Interaction root = Engine.creatInteraction(this.bot.getStory(), interaction, this.token);
					
					if(isHaveChildren(interaction)){
						log.info("Create child entities...");
						createChilds(interaction, root);
					}
				} catch (Exception e) {
					log.error("Error on create Child interaction", e.getMessage());
					if(log.isDebugEnabled()) {
						e.printStackTrace();
					}
				}
			});
	}

	private boolean isHaveChildren(Interaction interaction) {
		return interaction.getChilds() != null && interaction.getChilds().size() > 0 && interaction.getChilds().size() > 0;
	}
	
	private void createChilds(Interaction interaction, Interaction root) {
		interaction.getChilds().forEach(child ->{
			log.info("Root Entity Id: {} Child entity -> {}", root.getId(), child);
			try {
				Engine.createChildInteraction(this.bot.getStory(), root, child, this.token);
			} catch (BotException e) {
				e.printStackTrace();
			}
		});
	}

	private void createEntities(Interaction interaction) {
		if(interaction.getEntities() != null && interaction.getEntities().size() > 0){
			
			interaction.getEntities()
				.forEach(entity -> Engine.createEntity(entity, this.token));
		}
	}

	private void createReferences() {
		this.contexts.forEach(ctx -> {
			Engine.createReference(this.bot.getStory(), this.interactions, this.token, ctx);
		});
	}
	
	private InteractionAction addContext(ReferenceType referenceType, String interactionIdIn, String interactionIdOut, boolean root) {
		Context ctx = Context.builder()
				.id(interactionIdIn)
				.referenceType(referenceType)
				.contextOut(Arrays.asList(interactionIdOut))
				.references(Collections.emptyList())
				.root(root)
				.build();
		contexts.add(ctx);
		return this;
	}

}