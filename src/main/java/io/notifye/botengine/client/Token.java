package io.notifye.botengine.client;


import io.notifye.botengine.client.BotEngine;
import io.notifye.botengine.client.BotEngine.TokenType;
import lombok.Builder;
import lombok.Data;

@Builder
public @Data class Token {
	
	private String token;
	private TokenType tokenType;

}
