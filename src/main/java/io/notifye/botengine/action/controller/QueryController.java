package io.notifye.botengine.action.controller;

import static io.notifye.botengine.Engine.query;
import io.notifye.botengine.action.QueryAction;
import io.notifye.botengine.exception.QueryExecutionBotException;
import io.notifye.botengine.model.Query;
import io.notifye.botengine.model.QueryResponse;
import io.notifye.botengine.model.Story;
import io.notifye.botengine.security.Token;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final @Data class QueryController implements QueryAction {
	
	private final Story story;
	private final Token token;
	private final String session;
	
	@Override
	public QueryResponse q(String query) throws QueryExecutionBotException {
		return query(this.story, query, token, session);
	}

	@Override
	public QueryResponse q(Story story, String query) throws QueryExecutionBotException {
		return query(story, query, token, session);
	}

	@Override
	public QueryResponse q(Query query) throws QueryExecutionBotException {
		return query(query, token, session);
	}

	@Override
	public QueryResponse q(Story story, Query query) throws QueryExecutionBotException {
		return query(story, query, token, session);
	}

}