package com.pgjbz.twitch.loco.thread;

import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.listener.LocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class IrcListenerThreadTests {

    private BufferedReader bufferedReader;
    private TwitchConnection twitchConnection;
    private Socket socket;
    private List<LocoChatListener> chatListeners;
    private List<LocoIrcEventsListener> eventIrcListeners;
    private IrcListenerThread ircListenerThread;
    private LocoChatListener chatListener;


    @BeforeEach
    void setup() {
        bufferedReader = mock(BufferedReader.class);
        twitchConnection = mock(TwitchConnection.class);
        socket = mock(Socket.class);
        chatListener = mock(LocoChatListener.class);
        chatListeners = mock(ArrayList.class);
        eventIrcListeners = mock(ArrayList.class);
        ircListenerThread = new IrcListenerThread(bufferedReader,twitchConnection, socket, chatListeners, eventIrcListeners);
    }

    @Test
    void testIrcListenerThreadExpectedSuccess() throws Exception {
        when(socket.isConnected()).thenReturn(true);
        when(bufferedReader.readLine()).thenReturn(":teste!test@test.tmi.twitch.tv PRIVMSG #test :test");
        doNothing().when(chatListener).listenChat(any(ChatMessage.class));
        ircListenerThread.run();
        Thread.sleep(25);
        ircListenerThread.setKeepConnected(false);
        verify(socket).isConnected();
        verify(bufferedReader).readLine();
    }

    @Test
    void testIrcListenerThreadReconnectExpectedFail() throws Exception {
        when(socket.isConnected()).thenReturn(false);
        when(bufferedReader.readLine()).thenReturn(":teste!test@test.tmi.twitch.tv PRIVMSG #test :test");

        ircListenerThread.run();

        verify(socket, times(IrcListenerThread.RECONNECTION_ATTEMPTS + 1)).isConnected();
    }
}
