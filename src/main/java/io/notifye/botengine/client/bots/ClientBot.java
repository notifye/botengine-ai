package io.notifye.botengine.client.bots;

import io.notifye.botengine.client.Token;
import io.notifye.botengine.client.action.InteractionAction;
import io.notifye.botengine.client.action.QueryAction;
import io.notifye.botengine.client.action.StoriesAction;
import io.notifye.botengine.client.exception.BotUnsuportedOperationException;
import io.notifye.botengine.client.model.QueryResponse;
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
		super(token);
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
		return new QueryController(story);
	}
	
	public class QueryController implements QueryAction {
		
		private Story story;
		
		public QueryController(){}
		
		public QueryController(Story story){
			this.story = story;
		}

		@Override
		public QueryResponse q(String expression) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public QueryResponse q(Story story, String expression) {
			if(this.story == null){
				this.story = story;
			}
			return null;
		}

	}

}
