package io.notifye.botengine.client.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import io.notifye.botengine.client.action.QueryAction;
import io.notifye.botengine.client.bots.Bot;
import io.notifye.botengine.client.bots.BotEngine;
import io.notifye.botengine.client.bots.BotEngine.TokenType;
import io.notifye.botengine.client.bots.ClientBot;
import io.notifye.botengine.client.bots.DeveloperBot;
import io.notifye.botengine.client.model.Entity;
import io.notifye.botengine.client.model.Entry;
import io.notifye.botengine.client.model.Interaction;
import io.notifye.botengine.client.model.Parameter;
import io.notifye.botengine.client.model.QueryResponse;
import io.notifye.botengine.client.model.ResponseInteraction;
import io.notifye.botengine.client.model.Story;
import io.notifye.botengine.client.model.Synonym;
import io.notifye.botengine.client.model.enums.InteractionType;
import io.notifye.botengine.client.model.enums.ResponseInteractionType;
import io.notifye.botengine.client.security.Token;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BotTest {
	private static String devAccessToken = System.getenv("DEV_TOKEN");
	private static String clientAccessToken = System.getenv("CLIENT_TOKEN");
	
	private Bot developerBot;
	private Entry lordOfTheRingsEntry;
	private Entry startWarsEntry;
	
	private List<String> okResponses = Arrays.asList("Your reservation on $movies has been confirmed");
	private List<String> fallbackResponses = Arrays.asList(
							"I do not understand, could you repeat?", 
							"Please repeat", 
							"Excuse me, I'm still learning to understand humans. Could you please repeat what I said?", 
							"Damn, you're already fucking pissing me off.", 
							"I think I'm too stupid to understand you.",
							"Come on, help me. Try it another way!");
	
	@Before
	public void setup() throws Exception {
		// Create Entries for Entities
		lordOfTheRingsEntry = Entry.builder()
				.value("The Lord of the Rings")
				.synonyms(Arrays.asList(
						Synonym.builder().value("LOTR").build(),
						Synonym.builder().value("The Fellowship of the Ring").build(),
						Synonym.builder().value("The Lord of the Rings").build()))
				.build();
		
		startWarsEntry = Entry.builder()
				.value("Stars Wars")
				.synonyms(Arrays.asList(
						Synonym.builder().value("SW").build(),
						Synonym.builder().value("Stars Wars").build()))
				.build();
		
		// Configure Bot
		developerBot = BotEngine.ai(
				Token.builder()
					.token(devAccessToken)
					.tokenType(TokenType.DEV)
					.build())
				.stories()
				.create(Story.builder()
						.name("Movies reservation")
						.description("Movies reservation")
						.build())
				//.del()
				.bot();
	}
	
	//@After
	public void after() throws Exception{
		developerBot.stories().del();
	}
	
	@Test
	public void botTest() throws Exception {
		assertNotNull(developerBot);
		assertThat(developerBot)
			.describedAs("Bot is DeveloperBot instance").isInstanceOf(DeveloperBot.class);
		
		developerBot.interactions()
				.add()
					.welcome(Interaction.builder()
						.name("welcome interaction")
						.responses(Arrays.asList(
								ResponseInteraction.builder()
										.type(ResponseInteractionType.text)
										.messages(Arrays.asList(
												"Hello", 
												"Hey whats up", 
												"How can I help you?", 
												"Speak there noble fellow, how can I help you?", 
												"Please choose a movie"))
										.build()))
							.build())
					//Add hello interaction
					.interaction(Interaction.builder()
						.name("Hello")
						.action("hello")
						.userSays(Arrays.asList("Hello", "Hi"))
						.responses(Arrays.asList(ResponseInteraction.builder()
								.type(ResponseInteractionType.text)
								.messages(Arrays.asList("Please choose a movie"))
								.build()))
						.build())
					
					//chossing genre interaction
					.interaction(Interaction.builder()
						.name("choosing genre")
						.action("genre")
						.userSays(Arrays.asList("Do you play fantasy movies", "Do you play Science Fiction Movies"))
						.entities(Arrays.asList(Entity.builder()
								.name("movies")
								.entries(Arrays.asList(lordOfTheRingsEntry, startWarsEntry))
								.build()))
						.responses(Arrays.asList(ResponseInteraction.builder()
								.type(ResponseInteractionType.text)
								.messages(Arrays.asList("Yes of course! The best movies you found here!"))
								.build()))
						.build()
						.addChild(Interaction.builder()
							.name("choosing movies")
							.action("helloMovies")
							.userSays(Arrays.asList("the would be great to see @movies:movies", "I would like to go to @movies:movies", "@movies:movies"))
							.parameters(Arrays.asList(
									Parameter.builder()
										.alias("movies")
										.entity("@movies")
										.prompts(Arrays.asList("Which movie are you interested in?"))
										.build()))
							.responses(Arrays.asList(ResponseInteraction.builder()
									.type(ResponseInteractionType.text)
									.messages(okResponses)
									.build()))
								.build()))
					.fallback(Interaction.builder()
						.name("hello")
						.action("fallback")
						.triggers(Arrays.asList("defaultFallback"))
						.type(InteractionType.fallback)
						.responses(Arrays.asList(
								ResponseInteraction.builder()
										.type(ResponseInteractionType.text)
										.messages(fallbackResponses)
										.build()))
						.build())
					
					.referenceByName("Hello", "choosing movies")
				.build();
		
		// Switch Execution Mode and execute some query
		Bot clientBot = developerBot.switchToken(
			Token.builder()
				.token(clientAccessToken)
				.tokenType(TokenType.CLIENT)
				.build()
		);
			
		assertThat(clientBot)
			.describedAs("Bot is ClientBot instance").isInstanceOf(ClientBot.class);
		
		QueryAction conversation = clientBot.query(developerBot.getStory());
		
		QueryResponse queryResponse = conversation.q("Hello");
		assertNotNull(queryResponse);
		
		queryResponse = conversation.q("the would be great to see LOTR");
		assertNotNull(queryResponse);
		log.info("Query Result -> {}", queryResponse);
		
		QueryResponse fallbackResponse = conversation.q("Hey 0/");
		assertNotNull(fallbackResponse);
		log.info("Fallback 1 Query Result -> {}", fallbackResponse);
		
		fallbackResponse = conversation.q("I did not understand!!");
		assertNotNull(fallbackResponse);
		log.info("Fallback 2 Query Result -> {}", fallbackResponse);
		
	}

}
