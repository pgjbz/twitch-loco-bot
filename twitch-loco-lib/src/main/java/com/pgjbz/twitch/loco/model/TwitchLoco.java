package com.pgjbz.twitch.loco.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Getter
@Builder
public class TwitchLoco {

    private final String username;
    private final String oauth;
    @Setter
    private String channel;

    public TwitchLoco(String username, String oauth, String channel) {
        if(isBlank(username) || isBlank(oauth) || isBlank(channel))
            throw new IllegalArgumentException("Username, oauth and channel are mandatory");
        this.username = username;
        this.oauth = oauth;
        this.channel = channel;
    }
}
