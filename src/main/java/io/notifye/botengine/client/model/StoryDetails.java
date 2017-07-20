package io.notifye.botengine.client.model;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class StoryDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private List<Children> childrens;
	private List<InteractionDetails> interactions;

}
