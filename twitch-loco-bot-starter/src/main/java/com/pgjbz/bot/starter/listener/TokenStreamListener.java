package com.pgjbz.bot.starter.listener;

import com.pgjbz.bot.starter.chain.AbstractTokenChain;
import com.pgjbz.bot.starter.model.Token;
import com.pgjbz.bot.starter.model.pk.TokenPk;
import com.pgjbz.twitch.loco.listener.BotStreamInfoEventListener;
import com.pgjbz.twitch.loco.model.Chatters;
import com.pgjbz.twitch.loco.model.StreamInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
public class TokenStreamListener implements BotStreamInfoEventListener {

    private final AbstractTokenChain abstractTokenChain;

    @Override
    public void listenBotEvent(StreamInfo streamInfo) {
        final long start = System.currentTimeMillis();
        log.info("Event receive, starting process {} chatters...", streamInfo.getChatterCounter());
        final ExecutorService executorService = Executors.newFixedThreadPool(5);
        Chatters chatters = streamInfo.getChatters();
        List<String> viewers = getViewers(chatters);
        final List<Future<?>> futures = new CopyOnWriteArrayList<>();
        Optional<String> streamer = streamInfo.getChatters().getBroadcaster().stream().findAny();
        streamer.ifPresentOrElse(s ->
                futures.addAll(
                        viewers.stream().map(viewer -> executorService.submit(() ->
                            abstractTokenChain.doAddUnits(new Token(new TokenPk(viewer, s), null)))
                ).collect(Collectors.toList())), () -> log.info("Empty streamer..."));
        do
            futures.removeIf(Future::isDone);
        while (!futures.isEmpty());
        executorService.shutdown();
        log.info("Finish token distribution in {}ms", (System.currentTimeMillis() - start));
    }

    private List<String> getViewers(Chatters chatters) {
        log.info("Joining all chatters");
        List<String> viewers = chatters.getViewers();
        viewers.addAll(chatters.getModerators());
        viewers.addAll(chatters.getAdmins());
        viewers.addAll(chatters.getGlobalMods());
        viewers.addAll(chatters.getVips());
        viewers.addAll(chatters.getStaff());
        viewers.addAll(chatters.getBroadcaster());
        return viewers;
    }

}
