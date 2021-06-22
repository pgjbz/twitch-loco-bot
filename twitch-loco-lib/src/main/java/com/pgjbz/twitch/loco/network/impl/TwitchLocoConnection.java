package com.pgjbz.twitch.loco.network.impl;

import com.pgjbz.twitch.loco.enums.Command;
import com.pgjbz.twitch.loco.exceptions.TwitchLocoCommandException;
import com.pgjbz.twitch.loco.exceptions.TwitchLocoCommandParamException;
import com.pgjbz.twitch.loco.listeners.LocoChatListener;
import com.pgjbz.twitch.loco.listeners.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.TwitchLoco;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.NonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class TwitchLocoConnection extends TwitchConnection {

    private final Socket socket;
    private final InputStreamReader inputStreamReader;
    private final BufferedWriter bufferedWriter;
    private final TwitchLoco twitchLoco;
    private final List<LocoChatListener> chatListeners = new Vector<>();
    private final List<LocoIrcEventsListener> eventIrcListeners = new Vector<>();

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
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                while(socket.isConnected()) {
                    line = bufferedReader.readLine();
                    if(isBlank(line)) continue;
                    if(line.contains("PRIVMSG"))
                        for(LocoChatListener locoChatListener: chatListeners)
                            locoChatListener.listenChat(line);
                    else
                        for(LocoIrcEventsListener eventsListener: eventIrcListeners)
                            eventsListener.listenEvent(line);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        });

    }

    @Override
    public void sendMessage(@NonNull String message) {
        if(isBlank(message))
            throw new TwitchLocoCommandParamException("Message cannot be empty");
        sendCommand(Command.MESSAGE, this.twitchLoco.getChannel(),  message);
    }

    @Override
    public void sendCommand(@NonNull Command command, @NonNull  String... targets) {
        String commandPattern = command.getPattern();
        int paramsLength = commandPattern.replaceAll("[^$]", "").length();
        if(paramsLength != targets.length)
            throw new TwitchLocoCommandParamException(String.format("Invalid arguments length, command required exact %s arguments", paramsLength));
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
}
