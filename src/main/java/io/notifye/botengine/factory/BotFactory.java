package io.notifye.botengine.factory;

import io.notifye.botengine.bots.Bot;
import io.notifye.botengine.bots.ClientBot;
import io.notifye.botengine.bots.DeveloperBot;
import io.notifye.botengine.exception.SwitchBotException;
import io.notifye.botengine.security.Token;

public enum BotFactory {

	developerBot, clientBot;

	@SuppressWarnings("unchecked")
	public <T> T factory(Token token, double confidence, int lifespan) {
		switch (this) {
		case developerBot:
			return (T) DeveloperBot.builder()
					.token(token)
					.confidence(confidence)
					.lifespan(lifespan)
					.build();
		case clientBot:
			return (T) ClientBot.builder()
					.token(token)
					.confidence(confidence)
					.lifespan(lifespan)
					.build();
		default:
			return null;
		}
	}
	
	public interface TokenMode {
		public Bot switchToken(Token token) throws SwitchBotException;
	}
}