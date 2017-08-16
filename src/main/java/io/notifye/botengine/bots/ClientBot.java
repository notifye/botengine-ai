package io.notifye.botengine.bots;

import static io.notifye.botengine.bots.BotEngine.*;

import io.notifye.botengine.Engine;
import io.notifye.botengine.action.InteractionAction;
import io.notifye.botengine.action.QueryAction;
import io.notifye.botengine.action.StoriesAction;
import io.notifye.botengine.action.controller.QueryController;
import io.notifye.botengine.exception.BotUnsuportedOperationException;
import io.notifye.botengine.model.Story;
import io.notifye.botengine.security.Token;
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
