package io.notifye.botengine.client.action;

import io.notifye.botengine.client.model.Query;
import io.notifye.botengine.client.model.QueryResponse;
import io.notifye.botengine.client.model.Story;

public interface QueryAction {
	
	public QueryResponse q(String query);
	public QueryResponse q(Story story, String query);
	public QueryResponse q(Query query);
	public QueryResponse q(Story story, Query query);

}
