# Twitch loco bot
<hr>

#### What is this?

This project is a open source twitch irc bot, developed using in Java

<hr>

#### Purpose of this?

Learning more about sockets, practice my java skills and unit tests

<hr>

### What is need to run this project??

To run this project is necessary <a href="https://www.postgresql.org/">Postgreslq 13</a> with schema of database - schema is on twitch-loco-bot-starter folder - and <a href="https://openjdk.java.net/projects/jdk/11/">Java 11</a>

In twitch-loco-bot-starter has a docker-compose file with version of postgresql used in this project

To build this project is necessary <a href="https://maven.apache.org/download.cgi">Maven 3</a> and <a href="https://openjdk.java.net/projects/jdk/11/">Java 11</a>, steps:

- Clone project using ```git clone https://github.com/pgjbz/twitch-loco-bot.git```
- Use ```cd twitch-loco-bot``` to enter project folder
- Use command ```mvn clean install``` to build twitch-loco-lib and bot
- Use ```cd twitch-loco-bot-starter``` to enter bot folder
- Insert your twitch credentials and channel join to config.properties
- Copy file to target folder and use ```java -jar twitch-loco-bot.jar```
- If you prefer another folder to config.properties you can pass the location using ```java -jar twitch-loco-bot.jar <path-to-config>```

Example of config.properties file:

```
TWITCH_OAUTH_KEY=<your-oauth-key>
TWITCH_USERNAME=<your-username>
TWITCH_CHANNEL_JOIN=paulo97loco
DATABASE_NAME=<database-name-with-schemas>
DATABASE_USER=<database-username>
DATABASE_PASSWORD=<databse-password>
DATABASE_HOST=<database-host>
DATABASE_PORT=<database-port>
```