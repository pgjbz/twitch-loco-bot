package com.pgjbz.bot.starter.factory;

import com.pgjbz.twitch.loco.model.TwitchLoco;

import static com.pgjbz.bot.starter.configs.Configuration.getConfigs;

public class TwitchLocoFactory extends AbstractTwitchLocoFactory {

    TwitchLocoFactory(){}

    @Override
    public TwitchLoco createTwitchLoco() {
        String oauth = getConfigs().get("TWITCH_OAUTH_KEY");
        String username = getConfigs().get("TWITCH_USERNAME");
        String channel = getConfigs().get("TWITCH_CHANNEL_JOIN");
        return TwitchLoco.builder()
                .channel(channel)
                .username(username)
                .oauth(oauth).build();
    }

    @Override
    public TwitchLoco createTwitchLoco(String channel) {

        String oauth = getConfigs().get("TWITCH_OAUTH_KEY");
        String username = getConfigs().get("TWITCH_USERNAME");
        return TwitchLoco.builder()
                .channel(channel)
                .username(username)
                .oauth(oauth).build();
    }
}
