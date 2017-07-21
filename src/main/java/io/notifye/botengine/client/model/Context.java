package io.notifye.botengine.client.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@ToString
@AllArgsConstructor
public @Data class Context implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private boolean root;
	
	//References ?
	
	private List<String> contextOut;

}
