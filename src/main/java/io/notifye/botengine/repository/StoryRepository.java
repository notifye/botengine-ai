/**
 * 
 */
package io.notifye.botengine.repository;

import java.util.List;

import io.notifye.botengine.exception.BotException;
import io.notifye.botengine.model.Story;

/**
 * @author Adriano Santos
 *
 */
public interface StoryRepository {
	
	public Story getStory(String id) throws BotException;
	public Story getStoryByName(String name) throws BotException;
	public List<Story> getStories() throws BotException;
	public Story create(Story story) throws BotException;
	public void del(String id) throws BotException;
}
