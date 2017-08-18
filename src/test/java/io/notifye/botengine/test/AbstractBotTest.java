/**
 * 
 */
package io.notifye.botengine.test;

import io.notifye.botengine.bots.Bot;
import io.notifye.botengine.bots.BotEngine;
import io.notifye.botengine.exception.BotException;
import io.notifye.botengine.model.Story;
import io.notifye.botengine.security.Token;

/**
 * @author Adriano Santos
 *
 */
public abstract class AbstractBotTest {
	protected static String devAccessToken = System.getenv("DEV_TOKEN");
	protected static String clientAccessToken = System.getenv("CLIENT_TOKEN");
	protected Bot bot;
	
	public void setup() throws BotException, Exception {
		bot = BotEngine.ai(createToken())
				.stories()
				.create(createStory())
				.bot();
		
		createMessages();
	}
	
	public abstract void createMessages();

	/**
	 * @return Story
	 */
	public abstract Story createStory();

	/**
	 * @return Token
	 */
	public abstract Token createToken();
	
}
