package com.pgjbz.twitch.loco.model;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Builder
public class TwitchLoco {

    private final String username;
    private final String oauth;

    public TwitchLoco(String username, String oauth) {
        if(StringUtils.isBlank(username) || StringUtils.isBlank(oauth))
            throw new IllegalArgumentException("Username and oauth are mandatory");
        this.username = username;
        this.oauth = oauth;
    }
}
