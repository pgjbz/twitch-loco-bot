package com.pgjbz.twitch.loco.network;

import com.pgjbz.twitch.loco.enums.Command;
import com.pgjbz.twitch.loco.exceptions.TwitchLocoConnectionException;
import com.pgjbz.twitch.loco.listeners.LocoChatListener;
import com.pgjbz.twitch.loco.listeners.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.TwitchLoco;
import com.pgjbz.twitch.loco.network.impl.TwitchLocoConnection;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import static com.pgjbz.twitch.loco.constants.TwitchConstants.TWITCH_IRC_PORT;
import static com.pgjbz.twitch.loco.constants.TwitchConstants.TWITCH_IRC_URL;
import static com.pgjbz.twitch.loco.enums.Command.NICK;
import static com.pgjbz.twitch.loco.enums.Command.PASS;

public abstract class TwitchConnection {

    public abstract void sendMessage(String message);
    public abstract void sendCommand(Command command, String ...targets);
    public abstract void addChatListener(LocoChatListener chatListener);
    public abstract void addIrcEventListener(LocoIrcEventsListener ircEventsListener);
    public abstract void leaveChannel(String channel);
    public abstract void joinChannel(String channel);
    public abstract void startThreads();
    public abstract void close();

    public static TwitchLocoConnection getConnection(TwitchLoco twitchLoco){
        var socket = createSocket();
        var bufferedWriter = createBufferedWriter(socket);
        var inputStream = createInputStreamReader(socket);
        var twitchLocoConnection = new TwitchLocoConnection(socket, inputStream, bufferedWriter, twitchLoco);
        twitchLocoConnection.sendCommand(PASS, twitchLoco.getOauth());
        twitchLocoConnection.sendCommand(NICK, twitchLoco.getUsername());
        twitchLocoConnection.joinChannel(twitchLoco.getChannel());
        return twitchLocoConnection;
    }

    private static Socket createSocket() {
        try {
            SocketAddress socketAddress = new InetSocketAddress(TWITCH_IRC_URL, TWITCH_IRC_PORT);
            Socket socket = new Socket();
            socket.connect(socketAddress);
            return socket;
        } catch (IOException e){
            throw new TwitchLocoConnectionException(e.getMessage(), e.getCause());
        }
    }

    private static InputStreamReader createInputStreamReader(Socket socket) {
        try {
             return new InputStreamReader(socket.getInputStream());
        } catch (IOException e){
            throw new TwitchLocoConnectionException(e.getMessage(), e.getCause());
        }
    }

    private static BufferedWriter createBufferedWriter(Socket socket) {
        try {
            return new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e){
            throw new TwitchLocoConnectionException(e.getMessage(), e.getCause());
        }
    }
}
