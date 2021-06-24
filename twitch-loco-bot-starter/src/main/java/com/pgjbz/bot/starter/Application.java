package com.pgjbz.bot.starter;

import com.pgjbz.bot.starter.configs.Configuration;
import com.pgjbz.bot.starter.database.DB;
import com.pgjbz.bot.starter.database.jdbc.impl.JdbcTemplateImpl;
import com.pgjbz.bot.starter.model.TwitchUser;
import com.pgjbz.bot.starter.repository.TwitchUserRepository;
import com.pgjbz.bot.starter.repository.impl.TwitchUserRepositoryImpl;
import com.pgjbz.twitch.loco.listeners.LocoChatListener;
import com.pgjbz.twitch.loco.listeners.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.listeners.standards.StandardLocoChatListener;
import com.pgjbz.twitch.loco.listeners.standards.StandardLocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.model.TwitchLoco;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import com.pgjbz.twitch.loco.network.impl.TwitchLocoConnection;
import com.pgjbz.twitch.loco.util.ChatUtil;
import lombok.SneakyThrows;
import lombok.extern.java.Log;

import java.util.Optional;

@Log
public class Application {

    @SneakyThrows
    public static void main(String[] args) {

        DB.getConnection();

        String oauth = Configuration.getConfigs().get("TWITCH_OAUTH_KEY");
        String username = Configuration.getConfigs().get("TWITCH_USERNAME");
        String channel = Configuration.getConfigs().get("TWITCH_CHANNEL_JOIN");
        TwitchUserRepository twitchUserRepository = new TwitchUserRepositoryImpl(new JdbcTemplateImpl());

        TwitchLoco twitchLoco = TwitchLoco.builder()
                .channel(channel)
                .username(username)
                .oauth(oauth).build();
        TwitchLocoConnection connection = TwitchConnection.getConnection(
                twitchLoco
        );
        LocoChatListener chatListener = new StandardLocoChatListener();
        LocoIrcEventsListener ircEventsListener = new StandardLocoIrcEventsListener();
        connection.addChatListener(chatListener);
        connection.addChatListener(message -> {
            ChatMessage chatMessage = ChatUtil.extractFields(message);
            Optional<TwitchUser> optionalTwitchUser = twitchUserRepository.findByUsername(chatMessage.getUser());
            if(optionalTwitchUser.isEmpty()) {
                log.info(String.format("Inserting new user %s", chatMessage.getUser()));
                twitchUserRepository.insert(new TwitchUser(chatMessage.getUser()));
            }
        });
        connection.addIrcEventListener(ircEventsListener);
    }

}
