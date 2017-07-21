package io.notifye.botengine.client.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.notifye.botengine.client.BotEngine;
import io.notifye.botengine.client.BotEngine.TokenType;
import io.notifye.botengine.client.Token;
import io.notifye.botengine.client.action.QueryAction;
import io.notifye.botengine.client.bots.Bot;
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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BotTest {
	private static String clientAccessToken = System.getenv("CLIENT_TOKEN");
	private static String devAccessToken = System.getenv("DEV_TOKEN");
	
	private List<String> okResponses = Arrays.asList("Your reservation on $movies has been confirmed");
	
	private List<String> fallbackResponses = Arrays.asList(
							"Eu não entendi, você poderia repetir ?", 
							"Por favor repita", 
							"Desculpe-me, eu ainda estou aprendendo a entender seres humanos. Poderia, por gentileza, repetir o que disse ?", 
							"Porra você já está me enchenco o saco", 
							"Acho que sou burra demais para lhe entender.",
							"Vamos lá, me ajude. Tente de outra forma");
	
	private Bot developerBot;
	private Entry lordOfTheRingsEntry;
	private Entry startWarsEntry;
	
	@Before
	public void setup() throws Exception {
		// Create Entries
		lordOfTheRingsEntry = Entry.builder()
				.value("The Lord of the Rings")
				.synonyms(Arrays.asList(
						Synonym.builder()
							.value("LOTR")
							.build(),
						Synonym.builder()
							.value("The Fellowship of the Ring")
							.build(),
						Synonym.builder()
							.value("The Lord of the Rings")
							.build()))
				.build();
		
		startWarsEntry = Entry.builder()
				.value("Stars Wars")
				.synonyms(Arrays.asList(
						Synonym.builder()
							.value("SW")
							.build(),
						Synonym.builder()
							.value("Stars Wars")
							.build()))
				.build();
		
		// Configure Bot
		developerBot = BotEngine.ai(
				Token.builder()
					.token(devAccessToken)
					.tokenType(TokenType.DEV)
					.build())
			.stories()
				.create(Story.builder()
						.name("Example")
						.description("My first story")
						.build())
				//.del()
			.bot();
	}
	
	@After
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
														"Ola", 
														"E aí como vai", 
														"Como posso ajuda-lo", 
														"Fala aí nobre camarada, em que posso lhe ser útil?", 
														"Por favor escolha um filme"))
												.build()))
							 .build())
					.interaction(
							Interaction.builder()
								.name("choosing genre")
								.action("hello")
								.triggers(Arrays.asList("hello1"))
								.userSays(Arrays.asList("the would be great to see @movies:movies", "I would like to go to @movies:movies", "@movies:movies"))
								.entities(Arrays.asList(Entity.builder()
											.name("movies")
											.entries(Arrays.asList(lordOfTheRingsEntry, startWarsEntry))
											.build()))
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
								.build())
					
					//.interaction(Interaction.builder().build())
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
		
		QueryResponse queryResponse = conversation.q("the would be great to see LOTR");
		assertNotNull(queryResponse);
		log.info("Query Result -> {}", queryResponse);
		
		QueryResponse fallbackResponse = conversation.q("E ae 0/");
		assertNotNull(fallbackResponse);
		log.info("Fallback 1 Query Result -> {}", fallbackResponse);
		
		fallbackResponse = conversation.q("Nao entendi !!");
		assertNotNull(fallbackResponse);
		log.info("Fallback 2 Query Result -> {}", fallbackResponse);
		
	}

}
