package io.notifye.botengine.bots;

import io.notifye.botengine.action.ClientActions;
import io.notifye.botengine.action.DeveloperActions;
import io.notifye.botengine.exception.SwitchBotException;
import io.notifye.botengine.factory.BotFactory.TokenMode;
import io.notifye.botengine.model.Story;
import io.notifye.botengine.security.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public abstract class Bot implements TokenMode, DeveloperActions, ClientActions {
	public static final double DEFAULT_CONFIDENCE = 0.6;
	public static final int DEFAULT_LIFESPAN = 2;
	
	public static final String API_URL = "https://api.botengine.ai"; 
	
	@Getter
	private Token token;
	
	@Getter @Setter
	private double confidence;
	
	@Getter @Setter
	private int lifespan;
	
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
