package io.notifye.botengine.client.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@AllArgsConstructor
public @Data class Entity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private List<Entry> entries;
}
