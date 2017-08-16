package io.notifye.botengine.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public @Data class BaseStory implements Serializable {
	private static final long serialVersionUID = 1L;
	protected String id;
	protected String name;
	protected String description;
	 
}
