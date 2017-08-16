package io.notifye.botengine.action;

import java.util.List;

import io.notifye.botengine.bots.Bot;
import io.notifye.botengine.exception.BotException;
import io.notifye.botengine.model.Story;

public interface StoriesAction extends Action {
	
	public StoriesAction create(Story story) throws BotException;
	
	public List<Story> get() throws BotException;
	
	public Story get(String id) throws BotException;
	
	public Story getActual() throws BotException;
	
	public StoriesAction del(String id) throws BotException;
	
	public StoriesAction del() throws BotException;
	
	public Bot bot();

}
