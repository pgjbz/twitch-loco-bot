package com.pgjbz.bot.starter.repository;

import com.pgjbz.twitch.loco.model.IrcEvent;

public interface IrcEventRepository {

    boolean insert(IrcEvent ircEvent);
}
