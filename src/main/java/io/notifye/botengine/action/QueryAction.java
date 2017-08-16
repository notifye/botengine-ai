package io.notifye.botengine.action;

import io.notifye.botengine.model.Query;
import io.notifye.botengine.model.QueryResponse;
import io.notifye.botengine.model.Story;

public interface QueryAction {
	
	public QueryResponse q(String query);
	public QueryResponse q(Story story, String query);
	public QueryResponse q(Query query);
	public QueryResponse q(Story story, Query query);

}
