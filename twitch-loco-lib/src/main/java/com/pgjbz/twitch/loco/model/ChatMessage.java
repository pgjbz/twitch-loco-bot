package com.pgjbz.twitch.loco.model;

import static com.pgjbz.twitch.loco.util.CheckUtil.requireNotBlank;

public record ChatMessage (
    String message,
    String user,
    String channel) {

    public ChatMessage {
        requireNotBlank(message, "Message is mandatory");
        requireNotBlank(user, "User is mandatory");
        requireNotBlank(channel, " Channel is mandatory");
    }
}
