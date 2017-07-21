package io.notifye.botengine.client;


import io.notifye.botengine.client.BotEngine.TokenType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@ToString
public @Data class Token {
	private String token;
	private TokenType tokenType;
	
	public Token getTokenWithAuthorization(){
		this.setToken("Bearer ".concat(this.token)); 
		return this;
	}
}
