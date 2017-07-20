package io.notifye.botengine.client.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
public @Data class QueryResult implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String resolvedQuery;
	
	private Double confidence;
	
	private int score;
	
	private int lifespan;
	
	private boolean incomplete;
	
	private String storyId;
	
	private Interaction interaction;
	
	@JsonProperty("parameters")
	private Parameter parameter;
	
	private String fulfillment;

}
