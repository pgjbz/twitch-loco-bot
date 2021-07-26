package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.chain.AbstractIrcEventSaveChain;
import com.pgjbz.bot.starter.chain.IrcEventSaveChain;
import com.pgjbz.bot.starter.chain.IrcEventUserCheckChain;
import com.pgjbz.bot.starter.listener.IrcEventSaveListener;
import com.pgjbz.bot.starter.listener.NoticeIrcEventListener;
import com.pgjbz.bot.starter.listener.UserNoticeIrcEventListener;
import com.pgjbz.bot.starter.service.IrcEventService;
import com.pgjbz.bot.starter.service.UserService;
import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.NonNull;

public class IrcEventListenerFactory extends AbstractIrcEventListenerFactory {

    IrcEventListenerFactory(){}

    @Override
    public LocoIrcEventsListener createIrcEventSaveListener() {
        IrcEventService ircEventService = AbstractServiceFactory.getInstance().createIrcEventService();
        UserService userService = AbstractServiceFactory.getInstance().createUserService();
        AbstractIrcEventSaveChain checkUserIrc = new IrcEventUserCheckChain(userService);
        AbstractIrcEventSaveChain eventSaveChain = new IrcEventSaveChain(ircEventService);
        checkUserIrc.addNext(eventSaveChain);
        return new IrcEventSaveListener(checkUserIrc);
    }

    @Override
    public LocoIrcEventsListener createJoinChatListener(@NonNull TwitchConnection twitchConnection) {
        return null;
    }

    @Override
    public LocoIrcEventsListener createNoticeIrcEventListener(@NonNull TwitchConnection twitchConnection) {
        return new NoticeIrcEventListener(twitchConnection);
    }

    @Override
    public LocoIrcEventsListener createUserNoticeIrcEventListener(@NonNull TwitchConnection twitchConnection) {
        return new UserNoticeIrcEventListener(twitchConnection);
    }
}
