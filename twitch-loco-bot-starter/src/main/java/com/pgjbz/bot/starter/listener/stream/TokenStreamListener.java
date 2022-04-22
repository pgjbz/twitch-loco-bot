package com.pgjbz.bot.starter.listener.stream;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.pgjbz.bot.starter.chain.AbstractTokenChain;
import com.pgjbz.bot.starter.model.Token;
import com.pgjbz.bot.starter.model.pk.TokenPk;
import com.pgjbz.bot.starter.util.ListUtils;
import com.pgjbz.twitch.loco.listener.BotStreamInfoEventListener;
import com.pgjbz.twitch.loco.model.Chatters;
import com.pgjbz.twitch.loco.model.StreamInfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class TokenStreamListener implements BotStreamInfoEventListener {

    private static final int THREADS = 40;
    private final AbstractTokenChain abstractTokenChain;
    private static final ExecutorService executorService = Executors.newFixedThreadPool(THREADS);

    @Override
    public void listenBotEvent(StreamInfo streamInfo) {
        final long start = System.currentTimeMillis();
        log.info("Event receive, starting process {} chatters...", streamInfo.getChatterCounter());
        Chatters chatters = streamInfo.getChatters();
        final List<String> viewers = getViewers(chatters);

        final List<Future<?>> futures = Collections.synchronizedList(new ArrayList<>());

        Optional<String> streamer = streamInfo.getChatters().getBroadcaster().stream().findAny();

        streamer.ifPresentOrElse(
                s -> ListUtils.split(viewers.stream().map(viewer -> new Token(new TokenPk(viewer, s), null))
                        .collect(Collectors.toList()), THREADS)
                        .stream()
                        .map(list -> executorService.submit(() -> abstractTokenChain.doAddUnits(list))),
                () -> log.info("Empty streamer..."));

        do
            futures.removeIf(Future::isDone);
        while (!futures.isEmpty());
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
