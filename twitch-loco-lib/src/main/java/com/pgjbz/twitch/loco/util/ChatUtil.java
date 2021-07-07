package com.pgjbz.twitch.loco.util;

import com.pgjbz.twitch.loco.model.ChatMessage;
import lombok.extern.log4j.Log4j2;


@Log4j2
public class ChatUtil {

    private ChatUtil(){}

    public static ChatMessage extractFields(String messageReceive) {
        try {
            messageReceive = messageReceive.substring(messageReceive.indexOf(" :") + 2);
            String message = messageReceive.substring(messageReceive.indexOf(" :") + 2);
            String channel = messageReceive.substring(messageReceive.indexOf("#") + 1, messageReceive.indexOf(" :"));
            String user = messageReceive.substring(0, messageReceive.indexOf("!"));
            return new ChatMessage(message, user, channel);
        } catch (IllegalArgumentException e){
            log.error("Illegal argument exception with message: {}", messageReceive, e);
        } catch (Exception e) {
            log.error("Unexpected error {} - message receive: {}", e.getMessage(), messageReceive, e);
        }
        return null;
    }

}
