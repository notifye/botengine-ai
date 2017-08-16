package io.notifye.botengine.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public @Data final class Parameter implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String alias;
	//private Entity entity;
	private String entity;
	private List<String> prompts;
	private String webhook;
	

}
