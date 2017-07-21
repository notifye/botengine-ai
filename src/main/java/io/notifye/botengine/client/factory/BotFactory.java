package io.notifye.botengine.client.factory;

import io.notifye.botengine.client.Token;
import io.notifye.botengine.client.bots.Bot;
import io.notifye.botengine.client.bots.ClientBot;
import io.notifye.botengine.client.bots.DeveloperBot;
import io.notifye.botengine.client.exception.SwitchBotException;

public enum BotFactory {

	developerBot, clientBot;

	@SuppressWarnings("unchecked")
	public <T> T factory(Token token) {
		switch (this) {
		case developerBot:
			return (T) DeveloperBot.builder()
					.token(token)
					.build();
		case clientBot:
			return (T) ClientBot.builder()
					.token(token)
					.build();
		default:
			return null;
		}
	}
	
	public interface TokenMode {
		public Bot switchToken(Token token) throws SwitchBotException;
	}
}