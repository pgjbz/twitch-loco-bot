package com.pgjbz.bot.starter.thread;

import com.pgjbz.bot.starter.configs.BotConstants;
import com.pgjbz.bot.starter.util.BotUtils;
import com.pgjbz.twitch.loco.enums.CommandReceive;
import com.pgjbz.twitch.loco.model.IrcEvent;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import static java.lang.Thread.sleep;
import static java.util.Objects.isNull;

@Log4j2
@RequiredArgsConstructor
public class UserJoinThread implements Runnable {

    private final TwitchConnection twitchConnection;
    private static final Queue<IrcEvent> joinEvents = new LinkedList<>();
    private static final Random random = new Random();

    @Override
    public void run() {
        log.info("Starting message send for join users...");
        try {
            IrcEvent ircEvent = joinEvents.poll();
            if (isNull(ircEvent) ||
                    ircEvent.getCommandReceive() != CommandReceive.JOIN ||
                    BotUtils.isBot(ircEvent.getUsername())) return;
            String message = String.format("%s, %s",
                    ircEvent.getUsername(),
                    messages[random.nextInt(messages.length)]);
            log.info("Sending message: {}", message);
            twitchConnection.sendMessage(message);
            sleep(BotConstants.MESSAGE_INTERVAL);
        } catch (Exception e) {
            log.error("Error on sending join message: {}", e.getMessage(), e);
        }
    }

    public void addJoinEvent(IrcEvent ircEvent) {
        if(ircEvent.getCommandReceive() != CommandReceive.JOIN)
            return;
        log.info("Join event received, add to queue");
        joinEvents.add(ircEvent);
    }

    private final String[] messages = {
            "eae tranquilidade total ou nada bem?",
            "não seja timido, vamos interagir!",
            "eu acho que já te vi em algum lugar...",
            "oi, tudo bem?",
            "oi, vem sempre aqui? rs"
    };

}
