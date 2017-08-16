package io.notifye.botengine.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@ToString
@EqualsAndHashCode(callSuper = false)
public @Data class StoryResponse extends BaseStory {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private List<String> order;
	private List<StoryDetails> story;
	
	@Builder
	public StoryResponse(String id, String name, String description){
		super(id, name, description);
		this.name = name;
		this.description = description;
	}
	 
}
