package io.notifye.botengine.bots;

import java.util.Objects;

import io.notifye.botengine.action.InteractionAction;
import io.notifye.botengine.action.QueryAction;
import io.notifye.botengine.action.StoriesAction;
import io.notifye.botengine.action.controller.InteractionsActionController;
import io.notifye.botengine.action.controller.StoriesActionController;
import io.notifye.botengine.exception.BotUnsuportedOperationException;
import io.notifye.botengine.model.Story;
import io.notifye.botengine.security.Token;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = false)
public @Data class DeveloperBot extends Bot {
	private StoriesActionController storiesActionController;
	private InteractionsActionController interactionsActionController;
	
	@Builder
	public DeveloperBot(Token token) {
		super(token, DEFAULT_CONFIDENCE, DEFAULT_LIFESPAN);
	}
	
	@Builder
	public DeveloperBot(Token token, double confidence, int lifespan) {
		super(token, confidence, lifespan);
	}
	
	@Override
	public Story getStory(){
		return this.storiesActionController.getStory();
	}
	
	@Override
	public QueryAction query(Story story) throws Exception {
		throw new BotUnsuportedOperationException("Use Client Token for this action");
	}
	
	public StoriesAction stories() throws Exception {
		Token token = Objects.requireNonNull(getToken());
		if(this.storiesActionController == null){
			this.storiesActionController = new StoriesActionController(this, token);
		}
		return storiesActionController;
	}
	
	public InteractionAction interactions() throws Exception {
		Token token = Objects.requireNonNull(getToken());
		if(this.interactionsActionController == null){
			this.interactionsActionController = new InteractionsActionController(this, token);
		}
		return interactionsActionController;
	}

	@Override
	public QueryAction query(Story story, String session) throws Exception {
		throw new BotUnsuportedOperationException("Use Client Token for this action");
	}
	
}
