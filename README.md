Botengine.ai Java Client
===================

A idiomatic way to create chatbots through the BotEngine API 
----------

### Table of contents

1. [Introduction](#Introduction)
2. [Quickstart](#Quickstart)
3. [Show me the code](#Show-me-the-code)
4. [More Examples](#Examples)

Introduction<a name="Introduction"></a>
-------------

[Botengine](https://www.botengine.ai/) allows you to create a chatbot for any service. It's basically your personal chatbot factory. See more in this [page](https://www.botengine.ai/product-tour/).
Bot Engine java client enables you to create your chatbots in a much faster and more dynamic way, all through an idiomatic **java api!**

> **Note:**

> - BotEngine Service is a product of the LiveChat, Inc. See more about their policies by reading [this](https://www.botengine.ai/privacy-policy/).
> - BotEngine client java is being developed by [Notify-e](https://notifye.io) for our internal integrations. And now we are happy to share this with the community.
> - Bot Engine java client is not yet ready for production environments. Feel free to help us develop this library.

Quickstart<a name="Quickstart"></a>
-------------------

First you must create an account on the api website. Follow this [link](https://accounts.botengine.ai/signup) and take the opportunity to take the first steps [tutorial](https://app.botengine.ai/tutorial).

Now that you know how the tool works you are ready to start playing with the library.
To do this in your project start by adding the dependency:

```xml
<dependency>
  <groupId>com.github.notifye</groupId>
  <artifactId>botengine-ai</artifactId>
  <version>0.1.5-RELEASE</version>
</dependency>
```

Since we use jitpack you should add a new repository in your pom.xml

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```

> **Note:**

> - We assume you are using Maven, but it should be quiet to make these settings in other dependency managers such as sbt, gradle, or other.

Show me the code<a name="Show-me-the-code"></a>
-------------------

Now that everything is properly configured you can start coding your first chatbot.
We start by instantiating our bots by performing the basic settings:

```java
Bot bot = BotEngine.ai(Token.builder()
                      .token(System.getenv("BOT_DEV_TOKEN"))
                      .tokenType(TokenType.DEV)
                      .build())
         .stories()
                .create(Story.builder()
                             .name("C-3PO")
                             .description("See-Threepio Dialogues")
                             .build())
         .bot();
```

> **Note:**

> - BotEngine provides two types of access token. A (Develepor Token) for the development of your bot (try not to share this with everyone). And another one (Client Token) to use when running your bot. Remember to have these token on hand when following this quickstart :-)

Node code above we have created a new Bot called C-3PO. We did this by creating a new story. See more about stories in the official [documentation](https://docs.botengine.ai/key-concepts/introduction).

Now that you already have your Bot set up we should start creating the interactions:

```java
//FIRST CREATE SOME MESSAGES
List<String> c3poResponses = Arrays.asList(
                    "I suggest a new strategy, Artoo: let the Wookie win",
                    "Sir, it's very possible this asteroid is not stable",
                    "Do not you call me a mindless philosopher you overweight glob of grease!",
                    "Excuse me sir, but that R2-D2 is in prime condition, a real bargain");
        
        List<String> c3poFallbackResponses = Arrays.asList(
                    "We're doomed", "R2-D2, where are you?", "R2-D2, it's you, It's You!",
                    "If I told you half the things I've heard about this Jabba the Hutt, you'd probably short circuit",
                    "Die Jedi Dogs! Oh what did I say?",
                    "R2D2! You know better than to trust a strange computer!"
                    );

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
                      .entries(Arrays.asList(Entry.builder()
                            .value("Persons")
                            .synonyms(Arrays.asList(
                                        Synonym.builder()
                                          .value("Jedis")
                                          .build(),
                                        Synonym.builder()
                                          .value("Persons")
                                          .build()))
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
```

> **Note:**

> - Do not worry about the details, we will create more detailed documentation soon. The most important thing is to get familiar with the basics.

Now that you have created, configured, and taught your chatbot to talk, just interact with it a bit. For this, you can pass your client token.
Relax we have created a method for you to change token;)

```java
QueryResponse c3POResponse = bot.switchToken(
                Token.builder()
                       .token(System.getenv("CLIENT_TOKEN"))
                .tokenType(TokenType.CLIENT)
                .build())
                .query(bot.getStory())
                       .q("I would like to be a Jedi");

		assertNotNull(c3POResponse);
		c3POResponse.getResult().getFulfillment().stream().forEach(System.out::println);
```

**That's it for now! :)**

More Examples<a name="Examples"></a>
-------------------
[C3PO Simple Chat](https://github.com/notifye/botengine-ai-client/blob/master/src/test/java/io/notifye/botengine/test/C3POChatTest.java)

[Movies Reservation](https://github.com/notifye/botengine-ai-client/blob/master/src/test/java/io/notifye/botengine/test/BotTest.java)
