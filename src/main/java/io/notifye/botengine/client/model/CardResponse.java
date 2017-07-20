package io.notifye.botengine.client.model;

import java.io.Serializable;
import java.util.List;

import io.notifye.botengine.client.model.enums.CardType;
import lombok.Builder;
import lombok.Data;

@Builder
public @Data class CardResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private CardType type;
	private String title;
	private String subtitle;
	private String imageUrl;
	private List<Button> buttons;

}
