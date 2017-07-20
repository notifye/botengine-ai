package io.notifye.botengine.client.model;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data final class Parameter implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String alias;
	//private Entity entity;
	private String entity;
	private List<String> prompts;
	private String webhook;
	

}
