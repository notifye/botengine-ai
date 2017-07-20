package io.notifye.botengine.client.action;

import java.util.List;

import io.notifye.botengine.client.bots.Bot;
import io.notifye.botengine.client.exception.BotException;
import io.notifye.botengine.client.model.Story;

public interface StoriesAction extends Action {
	
	public StoriesAction create(Story story) throws BotException;
	
	public List<Story> get() throws BotException;
	
	public Story get(String id) throws BotException;
	
	public Story getActual() throws BotException;
	
	public StoriesAction del(String id) throws BotException;
	
	public StoriesAction del() throws BotException;
	
	public Bot bot();

}
