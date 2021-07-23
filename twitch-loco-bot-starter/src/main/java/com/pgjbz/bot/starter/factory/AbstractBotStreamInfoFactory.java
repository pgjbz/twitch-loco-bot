package com.pgjbz.bot.starter.factory;

import com.pgjbz.twitch.loco.listener.BotStreamInfoEventListener;

import static java.util.Objects.isNull;

public abstract class AbstractBotStreamInfoFactory {

    private static AbstractBotStreamInfoFactory instance;

    public static AbstractBotStreamInfoFactory getInstance() {
        if(isNull(instance))
            instance = new BotStreamInfoFactory();
        return instance;
    }

    public abstract BotStreamInfoEventListener createBotStreamTokenEventListener();
}
