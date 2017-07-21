package io.notifye.botengine.client.action.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import io.notifye.botengine.client.Engine;
import io.notifye.botengine.client.Token;
import io.notifye.botengine.client.action.InteractionAction;
import io.notifye.botengine.client.bots.Bot;
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
		welcomeInteraction.setType(InteractionType.welcome);
		this.interactions.add(welcomeInteraction);
		return this;
	}

	@Override
	public InteractionAction fallback(Interaction fallbackInteraction) {
		fallbackInteraction.setType(InteractionType.fallback);
		this.interactions.add(fallbackInteraction);
		return this;
	}

	@Override
	public InteractionAction interaction(Interaction userInteraction) {
		userInteraction.setType(InteractionType.user);
		this.interactions.add(userInteraction);
		return this;
	}
	
	@Override
	public Bot build() {
		Objects.requireNonNull(this.interactions, "Please necessary add interactions");
		this.interactions.stream()
			.forEach(interaction -> {
				
				log.info("Creating Entities...");
				if(interaction.getEntities() != null && interaction.getEntities().size() > 0){
					interaction.getEntities().forEach(entity -> {
						Engine.createEntity(entity, this.token);
					});
				}
				//CREATE INTERACTION
				log.info("Creating Interaction -> {}", interaction);
				Engine.creatInteraction(this.bot.getStory(), interaction, this.token);
			});
		
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
	
}