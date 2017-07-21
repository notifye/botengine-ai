package io.notifye.botengine.client.action.controller;

import java.util.List;

import io.notifye.botengine.client.Engine;
import io.notifye.botengine.client.Token;
import io.notifye.botengine.client.action.StoriesAction;
import io.notifye.botengine.client.bots.Bot;
import io.notifye.botengine.client.exception.BotException;
import io.notifye.botengine.client.model.Story;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
public final @Data class StoriesActionController implements StoriesAction {
	private static final long serialVersionUID = 1L;
	private final String ACTION_NAME = StoriesAction.class.getSimpleName();
	
	private final Bot bot;
	private final Token token;
	private Story story;
	
	@Override
	public StoriesAction create(Story story) throws BotException {
		this.story = story;
		this.story = Engine.createStory(story, this.token);
		return this;
	}

	@Override
	public List<Story> get() {
		//TODO: Implement GET All Stories
		return null;
	}

	@Override
	public Story get(String id) throws BotException {
		return Engine.getStory(id, this.token);
	}
	
	@Override
	public Story getActual() throws BotException {
		if(this.story == null){
			throw new BotException("Actual is not set");
		}
		return this.story;
	}

	@Override
	public StoriesAction del(String id) {
		Engine.deleteStory(id, token);
		return this;
	}
	
	@Override
	public StoriesAction del() throws BotException {
		return del(getActual().getId());
	}
	
	@Override
	public Bot bot() {
		return this.bot;
	}

	@Override
	public String getActionName() {
		return ACTION_NAME;
	}

	@Override
	public int getOrderExecution() {
		return -1;
	}
	
}
