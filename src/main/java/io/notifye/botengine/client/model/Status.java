package io.notifye.botengine.client.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
public @Data class Status implements Serializable {
	private static final long serialVersionUID = 1L;

	private int code;
	
	private String errorType;

}
