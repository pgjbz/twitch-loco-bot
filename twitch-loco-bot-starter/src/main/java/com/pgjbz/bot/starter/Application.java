package com.pgjbz.bot.starter;

import com.pgjbz.bot.starter.chain.AbstractChatSaveChain;
import com.pgjbz.bot.starter.chain.MessageSaveChain;
import com.pgjbz.bot.starter.chain.UserChatSaveChain;
import com.pgjbz.bot.starter.configs.Configuration;
import com.pgjbz.bot.starter.factory.AbstractMessageRepositoryFactory;
import com.pgjbz.bot.starter.factory.AbstractTwitchLocoFactory;
import com.pgjbz.bot.starter.factory.AbstractTwitchUserRepositoryFactory;
import com.pgjbz.bot.starter.listener.JoinChatListener;
import com.pgjbz.bot.starter.listener.SaveChatListener;
import com.pgjbz.twitch.loco.enums.Command;
import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.listener.standards.StandardBotStreamInfoEventListener;
import com.pgjbz.twitch.loco.listener.standards.StandardLocoChatListener;
import com.pgjbz.twitch.loco.listener.standards.StandardLocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.Chatters;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import com.pgjbz.twitch.loco.network.impl.TwitchLocoConnection;
import com.pgjbz.twitch.loco.schedule.BotStreamInfoEventSchedule;
import com.pgjbz.twitch.loco.service.impl.StreamInfoServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Random;

@Log4j2
public class Application {

    @SneakyThrows
    public static void main(String[] args) {

        log.info("Starting bot...");

        Configuration.setEnvironment(args);

        var messageRepository = AbstractMessageRepositoryFactory.getInstance().createMessageRepository();
        var twitchUserRepository = AbstractTwitchUserRepositoryFactory.getInstance().createTwitchUserRepository();
        AbstractChatSaveChain userChatSaveChain = new UserChatSaveChain(twitchUserRepository);
        AbstractChatSaveChain messageChatSaveChain = new MessageSaveChain(messageRepository);
        userChatSaveChain.addNext(messageChatSaveChain);
        LocoChatListener chatSaveListener = new SaveChatListener(userChatSaveChain);

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
        botStreamInfoEventSchedule.addBotStreamInfoEventListener(streamInfo -> {
            Chatters chatters = streamInfo.getChatters();
            List<String> viewers = chatters.getViewers();
            String message = "@" + viewers.get(random.nextInt(viewers.size())) + ", oi vem sempre aqui? rs";
            connection.sendMessage(message);
        });
        botStreamInfoEventSchedule.startSchedule(5L);
    }

}
