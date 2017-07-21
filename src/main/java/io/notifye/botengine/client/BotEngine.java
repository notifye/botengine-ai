package io.notifye.botengine.client;

import java.util.Objects;

import io.notifye.botengine.client.bots.Bot;
import io.notifye.botengine.client.factory.BotFactory;

public class BotEngine {
	
	public static <T extends Bot> Bot ai(Token token){
		Objects.requireNonNull(token, "Token is mandatory");
		
		switch (token.getTokenType()) {
		case DEV:
			return BotFactory.developerBot.factory(token);
		case CLIENT:
			return BotFactory.clientBot.factory(token);
		default:
			return null;
		}
	}
	
	public static enum TokenType {
		DEV, CLIENT
	}
}
