package com.pgjbz.bot.starter;

import com.pgjbz.bot.starter.chain.*;
import com.pgjbz.bot.starter.configs.Configuration;
import com.pgjbz.bot.starter.factory.AbstractMessageRepositoryFactory;
import com.pgjbz.bot.starter.factory.AbstractTokenRepositoryFactory;
import com.pgjbz.bot.starter.factory.AbstractTwitchLocoFactory;
import com.pgjbz.bot.starter.factory.AbstractTwitchUserRepositoryFactory;
import com.pgjbz.bot.starter.listener.JoinChatListener;
import com.pgjbz.bot.starter.listener.SaveChatListener;
import com.pgjbz.bot.starter.listener.TokenStreamListener;
import com.pgjbz.twitch.loco.enums.Command;
import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.listener.standards.StandardBotStreamInfoEventListener;
import com.pgjbz.twitch.loco.listener.standards.StandardLocoChatListener;
import com.pgjbz.twitch.loco.listener.standards.StandardLocoIrcEventsListener;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import com.pgjbz.twitch.loco.network.impl.TwitchLocoConnection;
import com.pgjbz.twitch.loco.schedule.BotStreamInfoEventSchedule;
import com.pgjbz.twitch.loco.service.impl.StreamInfoServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.util.Random;

@Log4j2
public class Application {

    @SneakyThrows
    public static void main(String[] args) {

        log.info("Starting bot...");

        Configuration.setEnvironment(args);

        var messageRepository = AbstractMessageRepositoryFactory.getInstance().createMessageRepository();
        var twitchUserRepository = AbstractTwitchUserRepositoryFactory.getInstance().createTwitchUserRepository();
        var tokenRepository = AbstractTokenRepositoryFactory.getInstance().createTokenRepository();
        AbstractChatSaveChain userChatSaveChain = new UserChatSaveChain(twitchUserRepository);
        AbstractChatSaveChain messageChatSaveChain = new MessageSaveChain(messageRepository);
        userChatSaveChain.addNext(messageChatSaveChain);
        LocoChatListener chatSaveListener = new SaveChatListener(userChatSaveChain);

        AbstractTokenChain userCheckTokenChain = new UserCheckTokenChain(twitchUserRepository);
        AbstractTokenChain createUnitTokenChain = new CreateUnitTokenChain(tokenRepository);
        AbstractTokenChain addUnitTokenChain = new AddUnitTokenChain(tokenRepository);

        userCheckTokenChain
                .addNext(createUnitTokenChain)
                .addNext(addUnitTokenChain);

        var twitchLoco = AbstractTwitchLocoFactory.getInstance().createTwitchLoco( "paulo97loco");
        TwitchLocoConnection connection = TwitchConnection.getConnection(twitchLoco);

        connection.startThread();

        LocoChatListener defaultChatListener = new StandardLocoChatListener();
        LocoIrcEventsListener defaultIrcEventsListener = new StandardLocoIrcEventsListener();
        LocoIrcEventsListener joinChannelListener = new JoinChatListener(connection);

        connection.addChatListener(defaultChatListener);
        connection.addChatListener(chatSaveListener);

        connection.addIrcEventListener(defaultIrcEventsListener);
        connection.addIrcEventListener(joinChannelListener);
        connection.addIrcEventListener(event -> {
            if(event.startsWith("PING"))
                connection.sendCommand(Command.PONG);
        });

        Random random = new Random();
        BotStreamInfoEventSchedule botStreamInfoEventSchedule = new BotStreamInfoEventSchedule(new StreamInfoServiceImpl(), twitchLoco);
        botStreamInfoEventSchedule.addBotStreamInfoEventListener(new StandardBotStreamInfoEventListener());
        botStreamInfoEventSchedule.addBotStreamInfoEventListener(new TokenStreamListener(userCheckTokenChain));
        botStreamInfoEventSchedule.startSchedule(1L);
    }

}
