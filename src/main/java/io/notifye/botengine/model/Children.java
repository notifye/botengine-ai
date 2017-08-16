package io.notifye.botengine.model;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
public @Data class Children implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private boolean ref;
	private List<Children> childrens;

}
