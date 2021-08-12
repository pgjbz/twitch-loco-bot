package com.pgjbz.twitch.loco.model;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Getter
@ToString
public class ChatMessage {

    private final String message;
    private final String user;
    private final String channel;
    private final Map<String, String> keys = new HashMap<>();

    public ChatMessage(String message, String user, String channel) {
        if(isBlank(message) || isBlank(user) || isBlank(channel))
            throw new IllegalArgumentException("Message, user and channel is mandatory");
        this.message = message;
        this.user = user;
        this.channel = channel;
    }
}
