package io.notifye.botengine.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
public final @Data class Entry implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String value;
	private List<Synonym> synonyms = new ArrayList<>();
	

}
