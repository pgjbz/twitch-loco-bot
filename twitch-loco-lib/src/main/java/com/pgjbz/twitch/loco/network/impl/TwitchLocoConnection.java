package com.pgjbz.twitch.loco.network.impl;

import com.pgjbz.twitch.loco.enums.Command;
import com.pgjbz.twitch.loco.exceptions.TwitchLocoCommandException;
import com.pgjbz.twitch.loco.exceptions.TwitchLocoCommandParamException;
import com.pgjbz.twitch.loco.listeners.LocoChatListener;
import com.pgjbz.twitch.loco.listeners.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.TwitchLoco;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.pgjbz.twitch.loco.constants.TwitchConstants.TWITCH_IRC_PORT;
import static com.pgjbz.twitch.loco.constants.TwitchConstants.TWITCH_IRC_URL;
import static com.pgjbz.twitch.loco.enums.Command.*;
import static java.lang.Math.abs;
import static java.lang.Math.pow;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class TwitchLocoConnection extends TwitchConnection {

    private static final int RECONNECTION_ATTEMPTS = 3;

    private final Socket socket;
    private final InputStreamReader inputStreamReader;
    private final BufferedWriter bufferedWriter;
    private final TwitchLoco twitchLoco;
    private final List<LocoChatListener> chatListeners = new Vector<>();
    private final List<LocoIrcEventsListener> eventIrcListeners = new Vector<>();
    /*
        reconnect is in test
     */
    private static boolean keepConnected = true;
    private BufferedReader bufferedReader;

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    public TwitchLocoConnection(
            @NonNull Socket socket,
            @NonNull InputStreamReader inputStreamReader,
            @NonNull BufferedWriter bufferedWriter,
            @NonNull TwitchLoco twitchLoco) {
        this.socket = socket;
        this.inputStreamReader = inputStreamReader;
        this.bufferedWriter = bufferedWriter;
        this.twitchLoco = twitchLoco;
        startThreads();
        executorService.shutdown();
    }

    private void startThreads() {
        executorService.submit(() -> {
            try {
                String line;
                this.bufferedReader = new BufferedReader(inputStreamReader);
                while(keepConnected) {
                    while (socket.isConnected() && keepConnected) {
                        line = bufferedReader.readLine();
                        if (isBlank(line)) continue;
                        if (line.contains("PRIVMSG"))
                            for (LocoChatListener locoChatListener : chatListeners)
                                locoChatListener.listenChat(line);
                        else
                            for (LocoIrcEventsListener eventsListener : eventIrcListeners)
                                eventsListener.listenEvent(line);
                    }
                    if(keepConnected)
                        tryReconnect(RECONNECTION_ATTEMPTS);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    @SneakyThrows
    private void tryReconnect(int reconnectionAttempts) {
        if(reconnectionAttempts == 0 || socket.isConnected()) return;
        System.out.println("Try to reconnect...");
        Thread.sleep((long)(pow(2.0, abs(reconnectionAttempts - RECONNECTION_ATTEMPTS))*1000));
        if(!socket.isConnected())
            socket.connect(new InetSocketAddress(TWITCH_IRC_URL, TWITCH_IRC_PORT));
        else
            sendCommand(RECONNECT);
        tryReconnect(--reconnectionAttempts);
    }

    @Override
    public void sendMessage(@NonNull String message) {
        if(isBlank(message))
            throw new TwitchLocoCommandParamException("Message cannot be empty");
        sendCommand(MESSAGE, this.twitchLoco.getChannel(),  message);
    }

    @Override
    public void sendCommand(@NonNull Command command, @NonNull  String... targets) {
        String commandPattern = command.getPattern();
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
        sendCommand(PART, channel);
    }

    @Override
    @SneakyThrows
    public void joinChannel(String channel) {
        leaveChannel(twitchLoco.getChannel());
        Thread.sleep(4000);
        twitchLoco.setChannel(channel);
        sendCommand(JOIN, channel);
    }

    @Override
    @SneakyThrows
    public void close() {
        keepConnected = false;
        leaveChannel(twitchLoco.getChannel());
        close(bufferedReader, bufferedWriter, inputStreamReader, socket);
    }

    @SneakyThrows
    private void close(BufferedReader bf, BufferedWriter bw, InputStreamReader in, Socket socket) {
        if(nonNull(bf))
            bf.close();
        if(nonNull(bw))
            bw.close();
        if(nonNull(in))
            in.close();
        if(nonNull(socket))
            socket.close();
    }
}
