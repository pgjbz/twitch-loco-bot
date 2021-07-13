package com.pgjbz.bot.starter;

import com.pgjbz.bot.starter.chain.*;
import com.pgjbz.bot.starter.configs.Configuration;
import com.pgjbz.bot.starter.factory.*;
import com.pgjbz.bot.starter.listener.*;
import com.pgjbz.twitch.loco.enums.CommandReceive;
import com.pgjbz.twitch.loco.enums.CommandSend;
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

import java.util.concurrent.atomic.AtomicReference;

import static java.util.Objects.nonNull;

@Log4j2
public class Application {

    /*
    * Initial tests
    * */
    @SneakyThrows
    public static void main(String[] args) {

        log.info("Starting bot...");

        Configuration.setEnvironment(args);

        var messageRepository = AbstractRepositoryFactory.getInstance().createMessageRepository();
        var tokenRepository = AbstractRepositoryFactory.getInstance().createTokenRepository();
        var userService = AbstractUserServiceFactory.getInstance().createUserService();
        var ircEventRepository = AbstractRepositoryFactory.getInstance().createIrcEventRepository();
        AbstractChatSaveChain userChatSaveChain = new UserChatSaveChain(userService);
        AbstractChatSaveChain messageChatSaveChain = new MessageSaveChain(messageRepository);
        userChatSaveChain.addNext(messageChatSaveChain);
        LocoChatListener chatSaveListener = new SaveChatListener(userChatSaveChain);

        AbstractTokenChain userCheckTokenChain = new UserCheckTokenChain(userService);
        AbstractTokenChain createUnitTokenChain = new CreateUnitTokenChain(tokenRepository);
        AbstractTokenChain addUnitTokenChain = new AddUnitTokenChain(tokenRepository);

        userCheckTokenChain
                .addNext(createUnitTokenChain)
                .addNext(addUnitTokenChain);

        var twitchLoco = AbstractTwitchLocoFactory.getInstance().createTwitchLoco();
        TwitchLocoConnection connection = TwitchConnection.getConnection(twitchLoco);

        connection.startThread();

        LocoChatListener defaultChatListener = new StandardLocoChatListener();
        LocoIrcEventsListener defaultIrcEventsListener = new StandardLocoIrcEventsListener();
        LocoIrcEventsListener joinChannelListener = new JoinChatListener(connection);

        AbstractIrcEventSaveChain checkUserIrc = new IrcEventUserCheckChain(userService);
        AbstractIrcEventSaveChain eventSaveChain = new IrcEventSaveChain(ircEventRepository);
        checkUserIrc.addNext(eventSaveChain);

        connection.addChatListener(defaultChatListener);
        connection.addChatListener(chatSaveListener);
        connection.addChatListener(new CommandChatListener(connection));

        connection.addIrcEventListener(new IrcEventSaveListener(checkUserIrc));
        connection.addIrcEventListener(defaultIrcEventsListener);
//        connection.addIrcEventListener(joinChannelListener);
        connection.addIrcEventListener(event -> {
            if(nonNull(event) && event.getCommandReceive() == CommandReceive.PING)
                connection.sendCommand(CommandSend.PONG);
        });

        AtomicReference<Long> messageSend = new AtomicReference<>(System.currentTimeMillis());

        connection.addIrcEventListener(event -> {
            try {
                if (nonNull(event) && event.getCommandReceive() == CommandReceive.USERNOTICE && (messageSend.get() + 30000) <= System.currentTimeMillis()) {
                    connection.sendMessage(event.getUsername() + " to só fazendo um teste!");
                    messageSend.set(System.currentTimeMillis());
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });

        //CAPs, not cap tags because is bad

        BotStreamInfoEventSchedule botStreamInfoEventSchedule = new BotStreamInfoEventSchedule(new StreamInfoServiceImpl(), twitchLoco);
        botStreamInfoEventSchedule.addBotStreamInfoEventListener(new StandardBotStreamInfoEventListener());
        botStreamInfoEventSchedule.addBotStreamInfoEventListener(new TokenStreamListener(userCheckTokenChain));
        botStreamInfoEventSchedule.startSchedule(5L);

    }

}
