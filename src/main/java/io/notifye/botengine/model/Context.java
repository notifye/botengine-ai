package io.notifye.botengine.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.notifye.botengine.model.Context.ReferenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author Adriano Santos
 *
 */
@Builder
@ToString
@AllArgsConstructor
public @Data class Context implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private boolean root;
	
	@JsonIgnore
	private ReferenceType referenceType;
	
	//References ?
	private List<String> references = new ArrayList<>();
	
	private List<String> contextOut = new ArrayList<>();
	
	public enum ReferenceType {
		BY_NAME, BY_ID;
	}

}
