package io.notifye.botengine.client.test;

import java.util.Arrays;

import io.notifye.botengine.client.BotEngine;
import io.notifye.botengine.client.BotEngine.TokenType;
import io.notifye.botengine.client.Token;
import io.notifye.botengine.client.bots.Bot;
import io.notifye.botengine.client.model.Entity;
import io.notifye.botengine.client.model.Entry;
import io.notifye.botengine.client.model.Interaction;
import io.notifye.botengine.client.model.Parameter;
import io.notifye.botengine.client.model.ResponseInteraction;
import io.notifye.botengine.client.model.Story;
import io.notifye.botengine.client.model.Synonym;
import io.notifye.botengine.client.model.enums.InteractionType;
import io.notifye.botengine.client.model.enums.ResponseInteractionType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BotTest {
	private static String clientAccessToken = "Bearer e5b129af8161ed369e27804f9a2735fa81a43d2e911e661195c6c7160b70b27b";
	private static String devAccessToken = "Bearer 9bc76810e6767d68970095e8db817a79134245b096eddcd645d34f19996751a1";
	
	public static void main(String[] args) throws Exception {
		
		// Configure Bot, Story and parametrized Interactions
		Bot developerBot = BotEngine.ai(
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
		
		Entry lordOfTheRingsEntry = Entry.builder()
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
		
		Entry startWarsEntry = Entry.builder()
									.value("Stars Wars")
									.synonyms(Arrays.asList(
											Synonym.builder()
												.value("SW")
												.build(),
											Synonym.builder()
												.value("Stars Wars")
												.build()))
									.build();
		
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
										.messages(Arrays.asList("Your reservation on $movies has been confirmed"))
										.build()))
								.build())
					
					
					//.interaction(Interaction.builder().build())
					//.interaction(Interaction.builder().build())
					//.interaction(Interaction.builder().build())
					.fallback(Interaction.builder()
							.name("hello")
							.action("fallback")
							.triggers(Arrays.asList("defaultFallback"))
							.type(InteractionType.fallback)
							.responses(Arrays.asList(
									ResponseInteraction.builder()
											.type(ResponseInteractionType.text)
											.messages(Arrays.asList(
													"Eu não entendi, você poderia repetir ?", 
													"Por favor repita", 
													"Desculpe-me, eu ainda estou aprendendo a entender seres humanos. Poderia, por gentileza, repetir o que disse ?", 
													"Porra você já está me enchenco o saco", 
													"Acho que sou burra demais para lhe entender.",
													"Vamos lá, me ajude. Tente de outra forma"))
											.build()))
						.build())
				.build();
		
		
		//Story story = developerBot.stories().get( developerBot.getStory().getId() );
		//log.info("Remote Story -> {}", story);
				
		
		/*List<Story> stories = developerBot.stories().get();
		log.info("Bot Stories -> {}", stories);
		
		stories.stream().forEach(s -> {
			try {
				Story story = developerBot.stories().get(s.getId());
				log.info("Story -> {}", story);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		
		// Switch Execution Mode and execute some query
		Bot clientBot = developerBot.switchToken(
			Token.builder()
				.token(clientAccessToken)
				.tokenType(TokenType.CLIENT)
				.build()
		);
		
		QueryResponse queryResponse = clientBot.query(developerBot.getStory()).q("Hi");
		log.info("Query Result -> {}", queryResponse);*/
		
	}

}
