package com.pgjbz.twitch.loco.model;

import com.pgjbz.twitch.loco.enums.CommandReceive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

import static com.pgjbz.twitch.loco.util.CheckUtil.requireNonNull;
import static com.pgjbz.twitch.loco.util.CheckUtil.requireNotBlank;

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
        this.commandReceive = requireNonNull(commandReceive, "Command receive is mandatory");
        this.username = requireNotBlank(username, "Username is mandatory");
        this.channel = requireNotBlank(channel, "Channel is mandatory");
    }
}
