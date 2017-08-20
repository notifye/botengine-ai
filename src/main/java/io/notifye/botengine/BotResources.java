/**
 * 
 */
package io.notifye.botengine;

/**
 * @author Adriano Santos
 *
 */
public interface BotResources {
	public static final String ENTITIES_RESOURCE = "%s/entities";
	public static final String STORIES_RESOURCE = "%s/stories";
	public static final String STORY_RESOURCE = "%s/stories/%s";
	public static final String REFERENCE_RESOURCE = STORY_RESOURCE + "/contexts";
	public static final String INTERACTIONS_RESOURCE = STORY_RESOURCE + "/interactions";
	public static final String WELCOME_RESOURCE = INTERACTIONS_RESOURCE + "/welcome";
	public static final String INTERACTIONS_CTX_RESOURCE = INTERACTIONS_RESOURCE + "/%s";
	public static final String FALLBACK_RESOURCE = INTERACTIONS_RESOURCE + "/fallback";
	public static final String QUERY_RESOURCE = "%s/query";

}
