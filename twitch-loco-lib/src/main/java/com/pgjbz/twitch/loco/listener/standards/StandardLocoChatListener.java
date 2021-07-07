package com.pgjbz.twitch.loco.listener.standards;

import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.model.ChatMessage;
import lombok.extern.log4j.Log4j2;

import static java.util.Objects.nonNull;

@Log4j2
public class StandardLocoChatListener implements LocoChatListener {


    /*
        Standard Chat listener only log chat
     */
    @Override
    public void listenChat(ChatMessage message) {
        if(nonNull(message))
            log.info(message.getUser() + " on " + message.getChannel() + " -> " + message.getMessage());
    }
}
