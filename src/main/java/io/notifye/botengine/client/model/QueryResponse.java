package io.notifye.botengine.client.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
public @Data class QueryResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("result")
	private QueryResult result;
	
	private String sessionId;
	
	//TODO: Define real type
	private String timestamp;
	
	private Status status;

}
