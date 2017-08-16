package io.notifye.botengine.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.notifye.botengine.model.enums.ResponseInteractionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@ToString
@AllArgsConstructor
public @Data class Fulfillment implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("type")
	private ResponseInteractionType type;
	
	@JsonProperty("message")
	private String message;

}
