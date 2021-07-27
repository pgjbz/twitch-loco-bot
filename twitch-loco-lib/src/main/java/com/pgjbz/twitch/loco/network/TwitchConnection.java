package com.pgjbz.twitch.loco.network;

import com.pgjbz.twitch.loco.enums.CommandSend;
import com.pgjbz.twitch.loco.exception.TwitchLocoConnectionException;
import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.TwitchLoco;
import com.pgjbz.twitch.loco.network.impl.TwitchLocoConnection;
import lombok.Setter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Date;
import java.util.List;

import static com.pgjbz.twitch.loco.constant.TwitchConstants.TWITCH_IRC_PORT;
import static com.pgjbz.twitch.loco.constant.TwitchConstants.TWITCH_IRC_URL;
import static com.pgjbz.twitch.loco.enums.CommandSend.NICK;
import static com.pgjbz.twitch.loco.enums.CommandSend.PASS;

public abstract class TwitchConnection {

    @Setter
    protected Long messageInterval = 30_000L;
    protected Date lastMessageSend;

    public abstract void sendMessage(String message);
    public abstract void sendCommand(CommandSend commandSend, String ...targets);
    public abstract void addChatListener(LocoChatListener chatListener);
    public abstract void addIrcEventListener(LocoIrcEventsListener ircEventsListener);
    public abstract void leaveChannel(String channel);
    public abstract void joinChannel(String channel);
    public abstract void startThread();
    public abstract void close();
    public abstract String getBotName();
    public abstract List<String> getModsList();
    public abstract void addMod(String username);
    public abstract void sendModListCommand();
    public abstract boolean canSendMessage(Date now, boolean isCommand);
    public abstract String getChannel();

    public static TwitchLocoConnection getConnection(TwitchLoco twitchLoco){
        var socket = createSocket();
        var bufferedWriter = createBufferedWriter(socket);
        var inputStream = createInputStreamReader(socket);
        var twitchLocoConnection = new TwitchLocoConnection(socket, inputStream, bufferedWriter, twitchLoco);
        executeBaseConfigs(twitchLoco, twitchLocoConnection);
        return twitchLocoConnection;
    }

    private static void executeBaseConfigs(TwitchLoco twitchLoco, TwitchLocoConnection twitchLocoConnection) {
        twitchLocoConnection.sendCommand(PASS, twitchLoco.getOauth());
        twitchLocoConnection.sendCommand(NICK, twitchLoco.getUsername());
        twitchLocoConnection.sendCommand(CommandSend.CAP_COMMANDS);
        twitchLocoConnection.sendCommand(CommandSend.CAP_MEMBERSHIP);
        twitchLocoConnection.sendCommand(CommandSend.CAP_TAG);
        twitchLocoConnection.joinChannel(twitchLoco.getChannel());
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
