package io.notifye.botengine.client.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.notifye.botengine.client.model.enums.InteractionType;
import lombok.Builder;
import lombok.Data;

@Builder
public @Data class Interaction implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonIgnore
	private String id;
	
	@JsonProperty("name")
	private String name;
	
	@JsonIgnore
	private InteractionType type;
	
	@JsonProperty("action")
	private String action;
	
	@JsonProperty("triggers")
	private List<String> triggers;
	
	@JsonProperty("userSays")
	private List<String> userSays;
	
	@JsonIgnore
	private List<Entity> entities;
	
	@JsonProperty("parameters")
	private List<Parameter> parameters;
	
	@JsonProperty("responses")
	private List<ResponseInteraction> responses;

	
}
