package com.pgjbz.twitch.loco.network.impl;

import com.pgjbz.twitch.loco.enums.CommandSend;
import com.pgjbz.twitch.loco.exception.TwitchLocoCommandException;
import com.pgjbz.twitch.loco.exception.TwitchLocoCommandParamException;
import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.TwitchLoco;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import com.pgjbz.twitch.loco.thread.IrcListenerThread;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.pgjbz.twitch.loco.enums.CommandSend.*;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Log4j2
public class TwitchLocoConnection extends TwitchConnection {

    private final Socket socket;
    private final InputStreamReader inputStreamReader;
    private final BufferedWriter bufferedWriter;
    private final TwitchLoco twitchLoco;

    private final List<LocoChatListener> chatListeners = new CopyOnWriteArrayList<>();
    private final List<LocoIrcEventsListener> eventIrcListeners = new CopyOnWriteArrayList<>();
    private List<String> modList;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private IrcListenerThread ircListenerThread;


    @Override
    public boolean canSendMessage(Date now, boolean isCommand) {
        return isNull(lastMessageSend) || now.getTime() > (lastMessageSend.getTime() + messageInterval) || isCommand;
    }

    @Override
    public String getChannel() {
        return twitchLoco.getChannel();
    }

    @Override
    public void sendModListCommand() {
        log.info("Sending mod update command");
        sendMessage("/mods");
    }

    public TwitchLocoConnection(
            @NonNull Socket socket,
            @NonNull InputStreamReader inputStreamReader,
            @NonNull BufferedWriter bufferedWriter,
            @NonNull TwitchLoco twitchLoco) {
        this.socket = socket;
        this.inputStreamReader = inputStreamReader;
        this.bufferedWriter = bufferedWriter;
        this.twitchLoco = twitchLoco;
    }

    public void startThread() {
        log.info("Starting thread");
        ircListenerThread = new IrcListenerThread(new BufferedReader(inputStreamReader),
                this, socket,
                chatListeners,
                eventIrcListeners);
        executorService.submit(ircListenerThread);
        executorService.shutdown();
    }

    @Override
    public void sendMessage(@NonNull String message) {
        if(isBlank(message))
            throw new TwitchLocoCommandParamException("Message cannot be empty");
        try {
            Date now = new Date(System.currentTimeMillis());
            boolean isCommand = message.startsWith("/");
            if (canSendMessage(now, isCommand)) {
                sendCommand(MESSAGE, this.twitchLoco.getChannel(), message);
                if(!isCommand)
                    lastMessageSend = now;
            } else
                log.info("Message cannot be send");
        } catch (Exception e) {
            log.error("Error on send message to irc: {}", e.getMessage(), e);
        }
    }

    @Override
    public void sendCommand(@NonNull CommandSend commandSend, @NonNull  String... targets) {
        String commandPattern = commandSend.getPattern();
        int paramsLength = commandPattern.replaceAll("[^$]", "").length();
        if(paramsLength != targets.length)
            throw new TwitchLocoCommandParamException(String.format("Invalid arguments length, command required exact %s arguments", paramsLength));
        if(targets.length > 0)
            for(int i = 1; i <= paramsLength; i++)
                commandPattern = commandPattern.replace("$"+i, targets[i-1]);
        try {
            bufferedWriter.write(commandPattern + "\r\n");
            bufferedWriter.flush();
        } catch (IOException e) {
            throw new TwitchLocoCommandException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public void addChatListener(LocoChatListener chatListener) {
        this.chatListeners.add(chatListener);
    }

    @Override
    public void addIrcEventListener(LocoIrcEventsListener ircEventsListener) {
        this.eventIrcListeners.add(ircEventsListener);
    }

    @Override
    public void leaveChannel(String channel) {
        if(isBlank(channel)) {
            log.info("Empty channel on leave channel");
            return;
        }
        log.info("Leaving channel {}", channel);
        sendCommand(PART, channel);
    }

    @Override
    public String getBotName() {
        return twitchLoco.getUsername();
    }

    @Override
    public List<String> getModsList() {
        return Collections.unmodifiableList(modList);
    }

    @Override
    public void addMod(String username) {
        if(isNull(modList)) modList = new ArrayList<>();
        if(isNotBlank(username))
            modList.add(username);
    }

    @Override
    public void joinChannel(String channel) {
        if(isBlank(channel)) {
            log.info("Empty channel on join channel");
            return;
        }
        leaveChannel(twitchLoco.getChannel());
        log.info("Join channel");
        twitchLoco.setChannel(channel);
        sendCommand(JOIN, channel);
        lastMessageSend = null;
        clearModList();
        sendModListCommand();
    }

    private void clearModList() {
        if(nonNull(modList))
            modList.clear();
    }

    @Override
    public void close() {
        leaveChannel(twitchLoco.getChannel());
        this.ircListenerThread.setKeepConnected(false);
        close(bufferedWriter, inputStreamReader, socket);
    }

    @SneakyThrows
    private void close(BufferedWriter bw, InputStreamReader in, Socket socket) {
        if(nonNull(bw))
            bw.close();
        if(nonNull(in))
            in.close();
        if(nonNull(socket))
            socket.close();
    }
}
