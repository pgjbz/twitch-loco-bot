package com.pgjbz.twitch.loco.util;

import com.pgjbz.twitch.loco.model.ChatMessage;

public class ChatUtil {

    private ChatUtil(){}

    public static ChatMessage extractFields(String messageReceive) {
        String[] fields = messageReceive.split(":");
        String message = fields[2];
        String channel = fields[1].substring(fields[1].indexOf("#") + 1);
        String user = messageReceive.split("!")[0].replace(":", "");
        return new ChatMessage(message, user, channel);
    }
}
