package com.pgjbz.twitch.loco.thread;

import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import com.pgjbz.twitch.loco.util.ChatUtil;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;

import static com.pgjbz.twitch.loco.constant.TwitchConstants.TWITCH_IRC_PORT;
import static com.pgjbz.twitch.loco.constant.TwitchConstants.TWITCH_IRC_URL;
import static com.pgjbz.twitch.loco.enums.CommandSend.RECONNECT;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Log4j2
@RequiredArgsConstructor
public class IrcListenerThread implements Runnable {

    private final BufferedReader bufferedReader;
    private final TwitchConnection twitchConnection;
    private final Socket socket;
    private final List<LocoChatListener> chatListeners;
    private final List<LocoIrcEventsListener> eventIrcListeners;

    public static final int RECONNECTION_ATTEMPTS = 3;
    @Setter
    private boolean keepConnected = true;

    @Override
    public void run() {
        log.info("Running thread");
        try(bufferedReader) {
            String line;
            while(keepConnected) {
                while (socket.isConnected() && keepConnected) {
                    line = bufferedReader.readLine();
                    if (isBlank(line)) continue;
                    if (line.contains("PRIVMSG")) {
                        ChatMessage message = ChatUtil.extractFields(line);
                        for (LocoChatListener locoChatListener : chatListeners)
                            locoChatListener.listenChat(message);
                    }
                    else
                        for (LocoIrcEventsListener eventsListener : eventIrcListeners)
                            eventsListener.listenEvent(line);
                }
                if(keepConnected)
                    tryReconnect(RECONNECTION_ATTEMPTS);

            }
        } catch (Exception e){
            log.error("Error on execute irc listen thread {}", e.getMessage(), e);
        }
    }

    @SneakyThrows
    private void tryReconnect(int reconnectionAttempts) {
        if(reconnectionAttempts == 0) {
            keepConnected = false;
            return;
        }
        log.info("Try to reconnect... remaining attempts {}", reconnectionAttempts);
        if(!socket.isConnected())
            socket.connect(new InetSocketAddress(TWITCH_IRC_URL, TWITCH_IRC_PORT));
        else
            twitchConnection.sendCommand(RECONNECT);
        Thread.sleep(5000L);
        tryReconnect(--reconnectionAttempts);
    }

}
