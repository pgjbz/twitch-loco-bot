package com.pgjbz.bot.starter;

import com.pgjbz.bot.starter.configs.Configuration;
import com.pgjbz.bot.starter.factory.AbstractBotStreamInfoFactory;
import com.pgjbz.bot.starter.factory.AbstractChatListenerFactory;
import com.pgjbz.bot.starter.factory.AbstractIrcEventListenerFactory;
import com.pgjbz.bot.starter.factory.AbstractTwitchLocoFactory;
import com.pgjbz.twitch.loco.model.TwitchLoco;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import com.pgjbz.twitch.loco.network.impl.TwitchLocoConnection;
import com.pgjbz.twitch.loco.schedule.BotStreamInfoEventSchedule;
import com.pgjbz.twitch.loco.service.impl.StreamInfoServiceImpl;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.util.Scanner;

@Log4j2
public class Application {

    /*
    * Initial tests
    * */
    @SneakyThrows
    public static void main(String[] args) {

        log.info("Starting bot...");

        Configuration.configEnvironment(args);

        TwitchLoco twitchLoco = AbstractTwitchLocoFactory.getInstance().createTwitchLoco();
        TwitchLocoConnection connection = TwitchConnection.getConnection(twitchLoco);

        connection.startThread();

        connection.addChatListener(AbstractChatListenerFactory.getInstance().createChatSaveLister());
        connection.addChatListener(AbstractChatListenerFactory.getInstance().createCommandChatListener(connection));
        connection.addIrcEventListener(AbstractIrcEventListenerFactory.getInstance().createIrcEventSaveListener());
        connection.addIrcEventListener(AbstractIrcEventListenerFactory.getInstance().createNoticeIrcEventListener(connection));
        connection.addIrcEventListener(AbstractIrcEventListenerFactory.getInstance().createUserNoticeIrcEventListener(connection));
        connection.addIrcEventListener(AbstractIrcEventListenerFactory.getInstance().createPongIrcEventListener(connection));

        BotStreamInfoEventSchedule botStreamInfoEventSchedule = new BotStreamInfoEventSchedule(new StreamInfoServiceImpl(), twitchLoco);
        botStreamInfoEventSchedule.addBotStreamInfoEventListener(
                AbstractBotStreamInfoFactory.getInstance().createBotStreamTokenEventListener()
        );
        botStreamInfoEventSchedule.startSchedule(5L);

        try(Scanner sc = new Scanner(System.in)) {
            String channelToJoin;
            while(!(channelToJoin = sc.next().strip()).isBlank())
                connection.joinChannel(channelToJoin);
        }
        connection.close();
        log.info("Exit bot");
        System.exit(0);

    }

}
