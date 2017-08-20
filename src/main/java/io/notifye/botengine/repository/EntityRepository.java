/**
 * 
 */
package io.notifye.botengine.repository;

import io.notifye.botengine.exception.BotException;
import io.notifye.botengine.model.Entity;

/**
 * @author Adriano Santos
 *
 */
public interface EntityRepository {
	
	public Entity createEntity(Entity entity) throws BotException;

}
