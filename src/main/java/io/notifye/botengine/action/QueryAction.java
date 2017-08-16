package io.notifye.botengine.action;

import io.notifye.botengine.exception.QueryExecutionBotException;
import io.notifye.botengine.model.Query;
import io.notifye.botengine.model.QueryResponse;
import io.notifye.botengine.model.Story;

public interface QueryAction {
	
	public QueryResponse q(String query) throws QueryExecutionBotException;
	public QueryResponse q(Story story, String query) throws QueryExecutionBotException;
	public QueryResponse q(Query query) throws QueryExecutionBotException;
	public QueryResponse q(Story story, Query query) throws QueryExecutionBotException;

}
