package io.notifye.botengine.client.security;


import io.notifye.botengine.client.bots.BotEngine.TokenType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@ToString
public @Data class Token {
	private String token;
	private TokenType tokenType;
}
