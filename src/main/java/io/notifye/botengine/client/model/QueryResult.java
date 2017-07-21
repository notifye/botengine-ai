package io.notifye.botengine.client.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
@JsonIgnoreProperties(value = {"parameters"}, ignoreUnknown = true)
public @Data class QueryResult implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String source;
	
	private String resolvedQuery;
	
	private Double confidence;
	
	private int score;
	
	private int lifespan;
	
	private boolean incomplete;
	
	private String storyId;
	
	private QueryInteractionResponse interaction;
	
	private List<Context> contexts;
	
	private List<Fulfillment> fulfillment;

}
