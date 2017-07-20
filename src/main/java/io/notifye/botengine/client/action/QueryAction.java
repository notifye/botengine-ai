package io.notifye.botengine.client.action;

import io.notifye.botengine.client.model.QueryResponse;
import io.notifye.botengine.client.model.Story;

public interface QueryAction {
	
	public QueryResponse q(String expression);
	public QueryResponse q(Story story, String expression);

}
