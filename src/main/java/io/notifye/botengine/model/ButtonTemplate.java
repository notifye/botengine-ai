package io.notifye.botengine.model;

import java.io.Serializable;
import java.util.List;

import io.notifye.botengine.model.enums.ButtonTemplateType;
import lombok.Builder;
import lombok.Data;

@Builder
public @Data class ButtonTemplate implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ButtonTemplateType type;
	private String title;
	private List<Button> buttons;

}
