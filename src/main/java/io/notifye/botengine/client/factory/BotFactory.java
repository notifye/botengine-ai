package io.notifye.botengine.client.factory;

import io.notifye.botengine.client.bots.Bot;
import io.notifye.botengine.client.bots.ClientBot;
import io.notifye.botengine.client.bots.DeveloperBot;
import io.notifye.botengine.client.exception.SwitchBotException;
import io.notifye.botengine.client.security.Token;

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