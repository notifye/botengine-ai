/**
 * 
 */
package io.notifye.botengine.repository;

import io.notifye.botengine.exception.BotException;
import io.notifye.botengine.model.Interaction;

/**
 * @author Adriano Santos
 *
 */
public interface InteractionRepository {
	
	public Interaction createUserInteraction(Interaction interaction) throws BotException;
	public Interaction createChildInteraction(Interaction root, Interaction child) throws BotException;
	public Interaction createOrUpdateWelcomeMessage(Interaction welcomeInteraction) throws BotException;
	public Interaction createOrUpdateFalbackInteraction(Interaction interaction) throws BotException;

}
