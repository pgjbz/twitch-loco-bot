package com.pgjbz.bot.starter.model.pk;

import lombok.Getter;
import lombok.ToString;

import static org.apache.logging.log4j.util.Strings.isBlank;

@Getter
@ToString
public class TokenPk {

    private final String username;
    private final String channel;

    public TokenPk(String username, String channel) {
        if(isBlank(username) ||isBlank(channel))
            throw new IllegalArgumentException(
                    String.format("Username and channel are mandatory - username receive %s | channel receive %s", username, channel)
            );
        this.username = username;
        this.channel = channel;
    }
}
