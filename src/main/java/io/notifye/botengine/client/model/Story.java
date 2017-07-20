package io.notifye.botengine.client.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Builder
@EqualsAndHashCode(callSuper = false)
public @Data class Story implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private String description;
	
	/*@Builder
	public Story(String id, String name, String description){
		super(id, name, description);
	}*/
	 
}
