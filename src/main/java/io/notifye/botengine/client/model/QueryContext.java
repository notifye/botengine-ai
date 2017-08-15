package io.notifye.botengine.client.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@ToString
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class QueryContext implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String name;
	
	//References ?
	//private List<Map<String, String>> parameters = new ArrayList<Map<String, String>>();
	//private List<Parameter> parameters = new ArrayList<>();
	

}
