package io.notifye.botengine.client.action.controller;

import io.notifye.botengine.client.Engine;
import io.notifye.botengine.client.Token;
import io.notifye.botengine.client.action.QueryAction;
import io.notifye.botengine.client.model.Query;
import io.notifye.botengine.client.model.QueryResponse;
import io.notifye.botengine.client.model.Story;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class QueryController implements QueryAction {
	
	private Story story;
	private Token token;
	private String session;
	
	@Override
	public QueryResponse q(String query) {
		return Engine.query(this.story, query, token, session);
	}

	@Override
	public QueryResponse q(Story story, String query) {
		if(this.story == null){
			this.story = story;
		}
		return Engine.query(story, query, token, session);
	}

	@Override
	public QueryResponse q(Query query) {
		return Engine.query(query, token, session);
	}

	@Override
	public QueryResponse q(Story story, Query query) {
		if(this.story == null){
			this.story = story;
		}
		return Engine.query(story, query, token, session);
	}

}