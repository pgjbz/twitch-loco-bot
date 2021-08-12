package com.pgjbz.bot.starter.factory;

import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.network.TwitchConnection;

import static java.util.Objects.isNull;

public abstract class AbstractChatListenerFactory {

    private static AbstractChatListenerFactory instance;

    public static AbstractChatListenerFactory getInstance() {
        if(isNull(instance))
            instance = new ChatListenerFactory();
        return instance;
    }

    public abstract LocoChatListener createChatSaveLister();
    public abstract LocoChatListener createCommandChatListener(TwitchConnection twitchConnection);
    public abstract LocoChatListener createBotTargetChatListener(TwitchConnection twitchConnection);
}
