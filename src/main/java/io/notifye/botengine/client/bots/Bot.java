package io.notifye.botengine.client.bots;

import io.notifye.botengine.client.BotEngine;
import io.notifye.botengine.client.Token;
import io.notifye.botengine.client.action.ClientActions;
import io.notifye.botengine.client.action.DeveloperActions;
import io.notifye.botengine.client.exception.SwitchBotException;
import io.notifye.botengine.client.factory.BotFactory.TokenMode;
import io.notifye.botengine.client.model.Story;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class Bot implements TokenMode, DeveloperActions, ClientActions {
	
	public static final String API_URL = "https://api.botengine.ai"; 
	
	@Getter
	private Token token;
	
	public abstract Story getStory();
	
	protected String getApiUrl(){
		return API_URL;
	}
	
	public Bot switchToken(Token token) throws SwitchBotException{
		if(token.equals(this.token)){
			throw new SwitchBotException("Token must be different from currently used token");
		}
		return BotEngine.ai(token);
	}

}
