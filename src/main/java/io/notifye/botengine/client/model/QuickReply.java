package io.notifye.botengine.client.model;

import java.io.Serializable;
import java.util.List;

import io.notifye.botengine.client.model.enums.QuickReplyType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public @Data class QuickReply implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private QuickReplyType type;
	private String title;
	private List<String> replies;
}
