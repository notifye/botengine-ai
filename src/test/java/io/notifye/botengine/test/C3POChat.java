/**
 * 
 */
package io.notifye.botengine.test;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;

import io.notifye.botengine.bots.Bot;
import io.notifye.botengine.bots.BotEngine;
import io.notifye.botengine.bots.BotEngine.TokenType;
import io.notifye.botengine.model.Entity;
import io.notifye.botengine.model.Entry;
import io.notifye.botengine.model.Interaction;
import io.notifye.botengine.model.Parameter;
import io.notifye.botengine.model.QueryResponse;
import io.notifye.botengine.model.ResponseInteraction;
import io.notifye.botengine.model.Story;
import io.notifye.botengine.model.Synonym;
import io.notifye.botengine.model.enums.InteractionType;
import io.notifye.botengine.model.enums.ResponseInteractionType;
import io.notifye.botengine.security.Token;

/**
 * @author Adriano Santos
 *
 */
public class C3POChat {
	
	private Bot bot;
	private List<String> c3poResponses;
	private List<String> c3poFallbackResponses;
	
	@Before
	public void setup() throws Exception {
		// Create instance of Bot
		if(Objects.isNull(bot)) {
			bot = BotEngine.ai(Token.builder()
	                .token(System.getenv("DEV_TOKEN"))
	                .tokenType(TokenType.DEV)
	                .build())
				  .stories()
				    .create(Story.builder()
				    	.name("C-3PO")
				        .description("See-Threepio Dialogues")
				        .build())
				   .bot();
			
			//NOW CREATE SOME MESSAGES
			createMessages();
		}
	}
	
	@Test
	public void chatBotTest() throws Exception {
		// AND NOW CONFIGURE IT !
		bot.interactions()
		     .add()
		        //WELCOME INTERACTION
			    .welcome(Interaction.builder()
			    .name("welcome on a Star Wars Universe")
			    .responses(Arrays.asList(
			         ResponseInteraction.builder()
			              .type(ResponseInteractionType.text)
			              .messages(Arrays.asList("Hello Jedi", "Hi. You are Stormtroopers ?"))
			         .build()))
		        .build())
			    //USER INTERACTION
		        .interaction(Interaction.builder()
		        .name("choosing a person")
		        .action("theForce")
		        .userSays(Arrays.asList("I would like to be a @persons:persons."))
		        .entities(Arrays.asList(Entity.builder()
		                      .name("persons")
		                      .entries(Arrays.asList(
		                    		  Entry.builder().value("Persons")
			                            .synonyms(Arrays.asList(
			                            		  Synonym.builder().value("Jedis").build(),
			                                      Synonym.builder().value("Persons").build()))
			                            .build()))
		                      .build()))
						      .parameters(Arrays.asList(
						                    Parameter.builder()
						                         .alias("persons")
						                         .entity("@persons")
						                         .prompts(Arrays.asList("Which person are you interested in?"))
						                    .build()))
						      .responses(Arrays.asList(ResponseInteraction.builder()
						                        .type(ResponseInteractionType.text)
						                        .messages(c3poResponses)
						                        .build()))
						                    .build())
		        .fallback(Interaction.builder()
		                          .name("hello")
		                          .action("fallback")
		                          .triggers(Arrays.asList("defaultFallback"))
		                          .type(InteractionType.fallback)
		                          .responses(Arrays.asList(
		                                 ResponseInteraction.builder()
		                                    .type(ResponseInteractionType.text)
		                                    .messages(c3poFallbackResponses)
		                                    .build()))
		                          .build())
		        .build();
		
		assertNotNull(bot);
		assertNotNull(bot.getStory());
		
		QueryResponse c3POResponse = bot.switchToken(
                Token.builder()
                       .token(System.getenv("CLIENT_TOKEN"))
                .tokenType(TokenType.CLIENT)
                .build())
                .query(bot.getStory())
                       .q("I would like to be a Jedi");

		assertNotNull(c3POResponse);
		c3POResponse.getResult().getFulfillment().stream().forEach(System.out::println);
	}
	
	private void createMessages() {
		c3poResponses = Arrays.asList(
                "I suggest a new strategy, Artoo: let the Wookie win",
                "Sir, it's very possible this asteroid is not stable",
                "Do not you call me a mindless philosopher you overweight glob of grease!",
                "Excuse me sir, but that R2-D2 is in prime condition, a real bargain");
		        
        c3poFallbackResponses = Arrays.asList(
                "We're doomed", "R2-D2, where are you?", "R2-D2, it's you, It's You!",
                "If I told you half the things I've heard about this Jabba the Hutt, you'd probably short circuit",
                "Die Jedi Dogs! Oh what did I say?",
                "R2D2! You know better than to trust a strange computer!");
		
	}
	

}
