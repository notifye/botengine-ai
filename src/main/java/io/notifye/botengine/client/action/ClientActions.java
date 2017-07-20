package io.notifye.botengine.client.action;

import io.notifye.botengine.client.model.Story;

public interface ClientActions {
	
	public QueryAction query(Story story) throws Exception;

}
