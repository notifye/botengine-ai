package io.notifye.botengine.client.model;

import java.io.Serializable;
import java.util.List;

import io.notifye.botengine.client.model.enums.ResponseInteractionType;
import lombok.Builder;
import lombok.Data;

@Builder
public @Data class ResponseInteraction implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ResponseInteractionType type;
	
	private List<String> messages;
	
	private QuickReply quickReply;
	
	private Image image;
	
	private CardResponse cardResponse;
	
	private ButtonTemplate buttonTemplate;
}
