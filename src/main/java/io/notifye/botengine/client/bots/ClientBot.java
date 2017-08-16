package io.notifye.botengine.client.bots;

import static io.notifye.botengine.client.BotEngine.*;

import io.notifye.botengine.client.Engine;
import io.notifye.botengine.client.Token;
import io.notifye.botengine.client.action.InteractionAction;
import io.notifye.botengine.client.action.QueryAction;
import io.notifye.botengine.client.action.StoriesAction;
import io.notifye.botengine.client.action.controller.QueryController;
import io.notifye.botengine.client.exception.BotUnsuportedOperationException;
import io.notifye.botengine.client.model.Story;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = false)
public @Data class ClientBot extends Bot {
	
	private Story story;
	
	@Builder
	public ClientBot(Token token) {
		super(token, DEFAULT_CONFIDENCE, DEFAULT_LIFESPAN);
	}
	
	@Builder
	public ClientBot(Token token, double confidence, int lifespan) {
		super(token, confidence, lifespan);
	}
	
	@Override
	public Story getStory(){
		return this.story;
	}

	@Override
	public StoriesAction stories() throws Exception {
		throw new BotUnsuportedOperationException("Use Developer Token for this action");
	}

	@Override
	public InteractionAction interactions() throws Exception {
		throw new BotUnsuportedOperationException("Use Developer Token for this action");
	}

	@Override
	public QueryAction query(Story story) throws Exception {
		return new QueryController(story, getToken(), Engine.getSession());
	}

	@Override
	public QueryAction query(Story story, String session) throws Exception {
		return new QueryController(story, getToken(), session);
	}
	
	

}
