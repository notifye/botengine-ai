package io.notifye.botengine.action;

import io.notifye.botengine.model.Story;

public interface ClientActions {
	
	public QueryAction query(Story story) throws Exception;
	public QueryAction query(Story story, String session) throws Exception;

}
