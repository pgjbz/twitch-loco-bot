package com.pgjbz.bot.starter;

import com.pgjbz.bot.starter.database.jdbc.JdbcTemplate;
import com.pgjbz.bot.starter.factory.AbstractJdbcTemplateFactory;
import com.pgjbz.bot.starter.model.Message;
import com.pgjbz.bot.starter.model.TwitchUser;
import com.pgjbz.bot.starter.repository.MessageRepository;
import com.pgjbz.bot.starter.repository.TwitchUserRepository;
import com.pgjbz.bot.starter.repository.impl.MessageRepositoryImpl;
import com.pgjbz.bot.starter.repository.impl.TwitchUserRepositoryImpl;
import com.pgjbz.twitch.loco.enums.Command;
import com.pgjbz.twitch.loco.listeners.LocoChatListener;
import com.pgjbz.twitch.loco.listeners.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.listeners.standards.StandardLocoChatListener;
import com.pgjbz.twitch.loco.listeners.standards.StandardLocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.TwitchLoco;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import com.pgjbz.twitch.loco.network.impl.TwitchLocoConnection;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;
import java.util.stream.Stream;

import static com.pgjbz.bot.starter.configs.BotConstants.CONFIG_FILE_SYSTEM_PROPERTY;
import static com.pgjbz.bot.starter.configs.Configuration.getConfigs;

@Log4j2
public class Application {

    @SneakyThrows
    public static void main(String[] args) {

        log.info("Starting bot...");

        String configFile = Stream.of(args).findFirst().orElse("");

        System.setProperty(CONFIG_FILE_SYSTEM_PROPERTY, configFile);

        String oauth = getConfigs(configFile).get("TWITCH_OAUTH_KEY");
        String username = getConfigs(configFile).get("TWITCH_USERNAME");
        String channel = getConfigs(configFile).get("TWITCH_CHANNEL_JOIN");

        JdbcTemplate jdbcTemplate = AbstractJdbcTemplateFactory.getInstance().createJdbcTemplate();
        TwitchUserRepository twitchUserRepository = new TwitchUserRepositoryImpl(jdbcTemplate);
        MessageRepository messageRepository = new MessageRepositoryImpl(jdbcTemplate);
        TwitchLoco twitchLoco = TwitchLoco.builder()
                .channel(channel)
                .username(username)
                .oauth(oauth).build();
        TwitchLocoConnection connection = TwitchConnection.getConnection(
                twitchLoco
        );
        connection.startThreads();
        LocoChatListener chatListener = new StandardLocoChatListener();
        LocoIrcEventsListener ircEventsListener = new StandardLocoIrcEventsListener();
        connection.addChatListener(chatListener);
        connection.addChatListener(message -> {

            //TODO decidir o pattern para este ponto, mais provavel que seja o Chain of Responsability
            Optional<TwitchUser> optionalTwitchUser = twitchUserRepository.findByUsername(message.getUser());
            if(optionalTwitchUser.isEmpty()) {
                log.info("Inserting new user {}", message.getUser());
                twitchUserRepository.insert(new TwitchUser(message.getUser()));
            }
            Message chatMessage = new Message(message);
            log.info("Saving message '{}' on database", message.toString());
            messageRepository.insert(chatMessage);
        });
        connection.addIrcEventListener(ircEventsListener);
        connection.addIrcEventListener(event -> {
            if(event.startsWith("PING"))
                connection.sendCommand(Command.PONG);
        });
    }

}
