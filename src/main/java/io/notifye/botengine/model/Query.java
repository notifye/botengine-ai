package io.notifye.botengine.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
public @Data class Query implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String query;
	
	private String sessionId;
	
	private Double confidence;
	
	private int lifespan;
	
	private boolean reset;
	
	private String storyId;
	
	private String trigger;
}
