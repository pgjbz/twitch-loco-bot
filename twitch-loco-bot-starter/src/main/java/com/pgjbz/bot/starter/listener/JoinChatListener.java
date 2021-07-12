package com.pgjbz.bot.starter.listener;

import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.IrcEvent;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import static java.util.Objects.nonNull;

@Log4j2
@RequiredArgsConstructor
public class JoinChatListener implements LocoIrcEventsListener {

    private final TwitchConnection twitchConnection;

    @Override
    public void listenEvent(IrcEvent event) {
        if(nonNull(event)) {
            String unknownString = event.getUnknownString();
            if (StringUtils.isNotBlank(unknownString) && unknownString.matches(".*366.*/NAMES\\slist"))
                twitchConnection.sendMessage("to na area, derrubou é penalti, sou um bot em teste, me ignore");
        }
    }
}
