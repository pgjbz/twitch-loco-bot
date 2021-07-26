package com.pgjbz.bot.starter.service;

import com.pgjbz.twitch.loco.model.IrcEvent;

public interface IrcEventService {

    boolean insert(IrcEvent ircEvent);
}
