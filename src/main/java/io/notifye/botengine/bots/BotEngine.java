package io.notifye.botengine.bots;

import static io.notifye.botengine.factory.BotFactory.*;

import java.util.Objects;

import io.notifye.botengine.security.Token;

public class BotEngine {
	
	public static <T extends Bot> Bot ai(Token token){
		return createBot(token, Bot.DEFAULT_CONFIDENCE, Bot.DEFAULT_LIFESPAN);
	}
	
	public static <T extends Bot> Bot ai(Token token, double confidence, int lifespan){
		return createBot(token, confidence, lifespan);
	}
	
	public static enum TokenType {
		DEV, CLIENT;
	}
	
	private static <T extends Bot> Bot createBot(Token token, double confidence, int lifespan) {
		Objects.requireNonNull(token, "Token is mandatory");
		switch (token.getTokenType()) {
		case DEV:
			return developerBot.factory(token, confidence, lifespan);
		case CLIENT:
			return clientBot.factory(token, confidence, lifespan);
		default:
			return null;
		}
	}
}
