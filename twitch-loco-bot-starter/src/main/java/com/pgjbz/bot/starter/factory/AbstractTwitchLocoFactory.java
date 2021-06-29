package com.pgjbz.bot.starter.factory;

import com.pgjbz.twitch.loco.model.TwitchLoco;

import static java.util.Objects.isNull;

public abstract class AbstractTwitchLocoFactory {

    private static AbstractTwitchLocoFactory instance;

    public abstract TwitchLoco createTwitchLoco();
    public abstract TwitchLoco createTwitchLoco(String channel);

    public static AbstractTwitchLocoFactory getInstance() {
        if(isNull(instance))
            instance = new TwitchLocoFactory();
        return instance;
    }
}
