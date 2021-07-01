package com.pgjbz.bot.starter.listener;

import com.pgjbz.bot.starter.chain.AbstractTokenChain;
import com.pgjbz.bot.starter.model.Token;
import com.pgjbz.bot.starter.model.pk.TokenPk;
import com.pgjbz.twitch.loco.listener.BotStreamInfoEventListener;
import com.pgjbz.twitch.loco.model.Chatters;
import com.pgjbz.twitch.loco.model.StreamInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
public class TokenStreamListener implements BotStreamInfoEventListener {

    private final AbstractTokenChain abstractTokenChain;

    @Override
    public void listenBotEvent(StreamInfo streamInfo) {
        log.info("Event receive, starting process {} chatters...", streamInfo.getChatterCounter());
        Chatters chatters = streamInfo.getChatters();
        chatters.getViewers().addAll(chatters.getModerators());
        chatters.getViewers().addAll(chatters.getAdmins());
        chatters.getViewers().addAll(chatters.getGlobalMods());
        chatters.getViewers().addAll(chatters.getVips());
        chatters.getViewers().addAll(chatters.getStaff());
        for(String viewer: chatters.getViewers()) {
            Optional<String> streamer = streamInfo.getChatters().getBroadcaster().stream().findAny();
            if(streamer.isEmpty()) {
                log.warn("No streamer founded");
                break;
            }
            abstractTokenChain.doAddUnits(new Token(new TokenPk(viewer, streamer.get()), null));
        }
    }
}
