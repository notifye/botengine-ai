package io.notifye.botengine.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Builder
public final @Data class Synonym implements Serializable {
	private static final long serialVersionUID = 1L;
	private String value;
}
