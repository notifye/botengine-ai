package io.notifye.botengine.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import io.notifye.botengine.action.QueryAction;
import io.notifye.botengine.bots.Bot;
import io.notifye.botengine.bots.BotEngine.TokenType;
import io.notifye.botengine.bots.ClientBot;
import io.notifye.botengine.bots.DeveloperBot;
import io.notifye.botengine.model.Entity;
import io.notifye.botengine.model.Entry;
import io.notifye.botengine.model.Interaction;
import io.notifye.botengine.model.Parameter;
import io.notifye.botengine.model.Query;
import io.notifye.botengine.model.QueryResponse;
import io.notifye.botengine.model.QuickReply;
import io.notifye.botengine.model.ResponseInteraction;
import io.notifye.botengine.model.Story;
import io.notifye.botengine.model.Synonym;
import io.notifye.botengine.model.enums.InteractionType;
import io.notifye.botengine.model.enums.ResponseInteractionType;
import io.notifye.botengine.security.Token;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BotTest extends AbstractBotTest {
	
	private Entry lordOfTheRingsEntry;
	private Entry startWarsEntry;
	
	private List<String> okResponses;
	private List<String> fallbackResponses;
	
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
		
		//Configure Bot
		super.setup();
		
	}
	
	@Test
	public void botTest() throws Exception {
		assertNotNull(bot);
		assertThat(bot)
			.describedAs("Bot is bot instance").isInstanceOf(DeveloperBot.class);
		
		bot.interactions()
			.add()
				.welcome(Interaction.builder()
					.name("welcome interaction")
					.responses(welcomeResponses())
				.build())
				//Add Choose Movie Interaction
				.interaction(helloInteraction())
				//chossing genre interaction
				.interaction(quickGenresInteraction())
				.interaction(chooseGenreInteraction())
				.fallback(fakkbackInteraction())
				.referenceByName("Hello", "choosing movies")
			.build();
		
		// Switch Execution Mode and execute some query
		Bot clientBot = bot.switchToken(
			Token.builder()
				.token(clientAccessToken)
				.tokenType(TokenType.CLIENT)
				.build()
		);
			
		assertThat(clientBot)
			.describedAs("Bot is ClientBot instance").isInstanceOf(ClientBot.class);
		
		QueryAction conversation = clientBot.query(bot.getStory());
		
		QueryResponse queryResponse = conversation.q("Hello");
		assertNotNull(queryResponse);
		
		queryResponse = conversation.q("the would be great to see LOTR");
		assertNotNull(queryResponse);
		log.info("Query Result -> {}", queryResponse);
		
		// Or use Query object to create queries
		queryResponse = conversation.q(
			Query.builder()
				.sessionId("12345678910")
				.storyId(bot.getStory().getId())
				.confidence(Bot.DEFAULT_CONFIDENCE) // or not set and leave defaults
				.lifespan(Bot.DEFAULT_LIFESPAN)     // or not set and leave defaults
				.trigger("hello")
				.build());
		
		QueryResponse fallbackResponse = conversation.q("Hey 0/");
		assertNotNull(fallbackResponse);
		log.info("Fallback 1 Query Result -> {}", fallbackResponse);
		
		fallbackResponse = conversation.q("I did not understand!!");
		assertNotNull(fallbackResponse);
		log.info("Fallback 2 Query Result -> {}", fallbackResponse);
		
	}
	
	@Override
	public Story createStory() {
		return Story.builder()
				.name("Movies reservation")
				.description("Movies reservation")
				.build();
	}
	
	@Override
	public Token createToken() {
		return Token.builder()
				.token(devAccessToken)
				.tokenType(TokenType.DEV)
				.build();
	}
	
	//@After
	public void after() throws Exception{
		bot.stories().del();
	}
	
	/* (non-Javadoc)
	 * @see io.notifye.botengine.test.AbstractBotTest#createMessages()
	 */
	@Override
	public void createMessages() {
		okResponses = Arrays.asList("Your reservation on $movies has been confirmed");
		fallbackResponses = Arrays.asList(
								"I do not understand, could you repeat?", 
								"Please repeat", 
								"Excuse me, I'm still learning to understand humans. Could you please repeat what I said?", 
								"Damn, you're already fucking pissing me off.", 
								"I think I'm too stupid to understand you.",
								"Come on, help me. Try it another way!");
		
	}
	
	private Interaction chooseGenreInteraction() {
		return Interaction.builder()
			.name("choosing genre")
			.action("genre")
			.userSays(Arrays.asList("Do you play fantasy movies", "Do you play Science Fiction Movies"))
			.entities(addEntities())
			.responses(Arrays.asList(ResponseInteraction.builder()
					.type(ResponseInteractionType.text)
					.messages(Arrays.asList("Yes of course! The best movies you found here!"))
					.build()))
			.build()
			.addChild(choosingMoviesInteraction());
	}

	private Interaction choosingMoviesInteraction() {
		return Interaction.builder()
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
				.build();
	}
	
	private List<Entity> addEntities() {
		return Arrays.asList(Entity.builder()
				.name("movies")
				.entries(Arrays.asList(lordOfTheRingsEntry, startWarsEntry))
				.build());
	}
	
	private Interaction fakkbackInteraction() {
		return Interaction.builder()
			.name("hello")
			.action("fallback")
			.triggers(Arrays.asList("defaultFallback"))
			.type(InteractionType.fallback)
			.responses(Arrays.asList(
					ResponseInteraction.builder()
							.type(ResponseInteractionType.text)
							.messages(fallbackResponses)
							.build()))
			.build();
	}
	
	private Interaction quickGenresInteraction() {
		return Interaction.builder()
				.name("genres")
				.action("genres")
				.userSays(Arrays.asList("Genres", "What are your genres?"))
				.responses(quickReplyGenres())
		.build();
	}

	private List<ResponseInteraction> quickReplyGenres() {
		return Arrays.asList(ResponseInteraction.builder()
				.type(ResponseInteractionType.quickReply)
				.quickReply(QuickReply.builder()
						.title("Choose")
						.replies(Arrays.asList("Science-Fiction", "Fantasy"))
						.build())
				.build());
	}

	private Interaction helloInteraction() {
		return Interaction.builder()
			.name("Hello")
			.action("hello")
			.triggers(Arrays.asList("hello"))
			.userSays(Arrays.asList("Hello", "Hi"))
			.responses(chooseMovie())
			.build();
	}

	private List<ResponseInteraction> chooseMovie() {
		return Arrays.asList(ResponseInteraction.builder()
				.type(ResponseInteractionType.text)
				.messages(Arrays.asList("Please choose a movie"))
				.build());
	}

	private List<ResponseInteraction> welcomeResponses() {
		return Arrays.asList(
			ResponseInteraction.builder()
					.type(ResponseInteractionType.text)
					.messages(Arrays.asList(
							"Hello", 
							"Hey whats up", 
							"How can I help you?", 
							"Speak there noble fellow, how can I help you?", 
							"Please choose a movie"))
					.build());
	}

}
