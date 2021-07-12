package com.pgjbz.twitch.loco.model;

import com.pgjbz.twitch.loco.enums.CommandReceive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Getter
@ToString
public class IrcEvent {

    private final CommandReceive commandReceive;
    private final String username;
    private final String channel;
    @Setter
    private String unknownString;
    private final Map<String, String> keys = new HashMap<>();

    public IrcEvent(CommandReceive commandReceive, String username, String channel) {
        if(isNull(commandReceive) || isBlank(username) || isBlank(channel))
            throw new IllegalArgumentException("Command receive, username and channel is mandatory |" + commandReceive + "|" + username + "|" + channel);
        this.commandReceive = commandReceive;
        this.username = username;
        this.channel = channel;
    }
}
