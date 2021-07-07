package com.pgjbz.twitch.loco.network.impl;

import com.pgjbz.twitch.loco.enums.CommandSend;
import com.pgjbz.twitch.loco.exception.TwitchLocoCommandException;
import com.pgjbz.twitch.loco.exception.TwitchLocoCommandParamException;
import com.pgjbz.twitch.loco.listener.standards.StandardLocoChatListener;
import com.pgjbz.twitch.loco.listener.standards.StandardLocoIrcEventsListener;
import com.pgjbz.twitch.loco.model.TwitchLoco;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import com.pgjbz.twitch.loco.thread.IrcListenerThread;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.MockedConstruction;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class TwitchLocoConnectionTests {

    private Socket socket;
    private BufferedWriter bufferedWriter;
    private TwitchLoco twitchLoco;
    private InputStreamReader inputStreamReader;
    private TwitchConnection twitchConnection;

    @BeforeEach
    void setup() {
        socket = mock(Socket.class);
        bufferedWriter = mock(BufferedWriter.class);
        twitchLoco = mock(TwitchLoco.class);
        inputStreamReader = mock(InputStreamReader.class);
        twitchConnection = new TwitchLocoConnection(socket, inputStreamReader, bufferedWriter, twitchLoco);
    }

    @ParameterizedTest
    @EnumSource(CommandSend.class)
    void testSendCommandExpectedSuccess(CommandSend commandSend) throws Exception {
        int paramsLength = getCommandArgumentsLength(commandSend, false);
        String[] stringArr = new String[paramsLength];

        for(int i = 0; i < paramsLength; i++)
            stringArr[i] = ""+i;

        doNothing().when(bufferedWriter).write(anyString());
        doNothing().when(bufferedWriter).flush();
        twitchConnection.sendCommand(commandSend, stringArr);
        verify(bufferedWriter).write(anyString());
        verify(bufferedWriter).flush();
    }

    @ParameterizedTest
    @EnumSource(CommandSend.class)
    void testSendCommandExpectedTwitchLocoCommandParamException(CommandSend commandSend) {
        int paramsLength = getCommandArgumentsLength(commandSend, true);
        String[] stringArr = new String[paramsLength > 0 ? paramsLength : 2];

        for(int i = 0; i < paramsLength; i++)
            stringArr[i] = ""+i;
        assertThrows(TwitchLocoCommandParamException.class, () -> twitchConnection.sendCommand(commandSend, stringArr));
    }

    @ParameterizedTest
    @EnumSource(CommandSend.class)
    void testSendCommandExpectedTwitchLocoCommandException(CommandSend commandSend) throws Exception {
        int paramsLength = getCommandArgumentsLength(commandSend, false);
        String[] stringArr = new String[paramsLength];

        for(int i = 0; i < paramsLength; i++)
            stringArr[i] = ""+i;

        doThrow(IOException.class).when(bufferedWriter).write(anyString());

        assertThrows(TwitchLocoCommandException.class, () -> twitchConnection.sendCommand(commandSend, stringArr));
        verify(bufferedWriter).write(anyString());
    }

    @Test
    void testSendCommandNullCommandExpectedNullPointerException() {
        assertThrows(NullPointerException.class, () -> twitchConnection.sendCommand(null, "string"));
    }

    @Test
    void testSendCommandNullArrayExpectedNullPointerException() {
        assertThrows(NullPointerException.class, () -> twitchConnection.sendCommand(CommandSend.MESSAGE, null));
    }

    private int getCommandArgumentsLength(CommandSend commandSend, boolean mismatch) {
        String commandPattern = commandSend.getPattern();
        return commandPattern.replaceAll("[^$]", "").length() - (mismatch ? 1 : 0 );
    }

    @Test
    void testSendMessageExpectedNullPointerException(){
        assertThrows(NullPointerException.class, () -> twitchConnection.sendMessage(null));
    }

    @Test
    void testSendMessageExpectedTwitchLocoCommandParamException(){
        assertThrows(TwitchLocoCommandParamException.class, () -> twitchConnection.sendMessage(""));
    }

    @Test
    void testSendMessageExpectedSuccess() throws Exception {
        doNothing().when(bufferedWriter).write(anyString());
        doNothing().when(bufferedWriter).flush();
        when(twitchLoco.getChannel()).thenReturn("channel");
        twitchConnection.sendMessage("message");

        verify(bufferedWriter).write(anyString());
        verify(bufferedWriter).flush();
    }

    @Test
    void testStartThreadExpectedSuccess()  {
        try(MockedConstruction<IrcListenerThread> mockedConstruction = mockConstruction(IrcListenerThread.class, (mock, ctx) ->
                doNothing().when(mock).run())) {
            twitchConnection.startThread();
            Runnable run = mockedConstruction.constructed().get(0);
            verify(run).run();
        }
    }

    @Test
    void testCloseConnectionExpectedSuccess() throws Exception {
        try(MockedConstruction<IrcListenerThread> mockedConstruction = mockConstruction(IrcListenerThread.class, (mock, ctx) ->
                doNothing().when(mock).run())) {

            when(twitchLoco.getChannel()).thenReturn("channel");
            twitchConnection.startThread();
            twitchConnection.close();

            Runnable run = mockedConstruction.constructed().get(0);
            verify(run).run();
            verify(socket).close();
            verify(inputStreamReader).close();
            verify(bufferedWriter).close();
        }
    }

    @Test
    void addChaListenerExpectedSuccess() {
        twitchConnection.addChatListener(new StandardLocoChatListener());
    }

    @Test
    void addIrcListenerExpectedSuccess() {
        twitchConnection.addIrcEventListener(new StandardLocoIrcEventsListener());
    }

    @Test
    void testJoinChannelExpectedSuccess() {
        twitchConnection.joinChannel("channel");
    }

}
