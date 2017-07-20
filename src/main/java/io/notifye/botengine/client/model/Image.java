package io.notifye.botengine.client.model;

import java.io.Serializable;

import io.notifye.botengine.client.model.enums.ImageType;
import lombok.Builder;
import lombok.Data;

@Builder
public @Data class Image implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ImageType type;
	private String imageUrl;
}
