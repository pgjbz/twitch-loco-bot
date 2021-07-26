package com.pgjbz.bot.starter.factory;

import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.NonNull;

import static java.util.Objects.isNull;

public abstract class AbstractIrcEventListenerFactory {

    private static AbstractIrcEventListenerFactory instance;

    public static AbstractIrcEventListenerFactory getInstance() {
        if(isNull(instance))
            instance = new IrcEventListenerFactory();
        return instance;
    }

    public abstract LocoIrcEventsListener createIrcEventSaveListener();
    public abstract LocoIrcEventsListener createJoinChatListener(@NonNull TwitchConnection twitchConnection);
    public abstract LocoIrcEventsListener createNoticeIrcEventListener(@NonNull TwitchConnection twitchConnection);
    public abstract LocoIrcEventsListener createUserNoticeIrcEventListener(@NonNull TwitchConnection twitchConnection);
    public abstract LocoIrcEventsListener createPongIrcEventListener(@NonNull TwitchConnection twitchConnection);

}
