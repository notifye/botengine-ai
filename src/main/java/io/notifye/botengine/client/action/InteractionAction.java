package io.notifye.botengine.client.action;

import io.notifye.botengine.client.bots.Bot;
import io.notifye.botengine.client.model.Interaction;

public interface InteractionAction extends Action {
	
	public InteractionAction add();
	
	public InteractionAction welcome(Interaction welcomeInteraction);
	
	public InteractionAction fallback(Interaction fallbackInteraction);
	
	public InteractionAction interaction(Interaction interaction);

	public InteractionAction referenceById(String interactionIdIn, String interactionIdOut);
	public InteractionAction referenceById(String interactionIdIn, String interactionIdOut, boolean root);
	
	public InteractionAction referenceByName(String interactionNameIn, String interactionNameOut);
	public InteractionAction referenceByName(String interactionNameIn, String interactionNameOut, boolean root);
	
	public Bot build();

}
