package com.pgjbz.twitch.loco.network.impl;

import com.pgjbz.twitch.loco.enums.Command;
import com.pgjbz.twitch.loco.exceptions.TwitchLocoCommandException;
import com.pgjbz.twitch.loco.exceptions.TwitchLocoCommandParamException;
import com.pgjbz.twitch.loco.model.TwitchLoco;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

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
    @EnumSource(Command.class)
    void testSendCommandExpectedSuccess(Command command) throws Exception {
        int paramsLength = getCommandArgumentsLength(command, false);
        String[] stringArr = new String[paramsLength];

        for(int i = 0; i < paramsLength; i++)
            stringArr[i] = ""+i;

        doNothing().when(bufferedWriter).write(anyString());
        doNothing().when(bufferedWriter).flush();
        twitchConnection.sendCommand(command, stringArr);
        verify(bufferedWriter).write(anyString());
        verify(bufferedWriter).flush();
    }

    @ParameterizedTest
    @EnumSource(Command.class)
    void testSendCommandExpectedTwitchLocoCommandParamException(Command command) {
        int paramsLength = getCommandArgumentsLength(command, true);
        String[] stringArr = new String[paramsLength];

        for(int i = 0; i < paramsLength; i++)
            stringArr[i] = ""+i;
        assertThrows(TwitchLocoCommandParamException.class, () -> twitchConnection.sendCommand(command, stringArr));
    }

    @ParameterizedTest
    @EnumSource(Command.class)
    void testSendCommandExpectedTwitchLocoCommandException(Command command) throws Exception {
        int paramsLength = getCommandArgumentsLength(command, false);
        String[] stringArr = new String[paramsLength];

        for(int i = 0; i < paramsLength; i++)
            stringArr[i] = ""+i;

        doThrow(IOException.class).when(bufferedWriter).write(anyString());

        assertThrows(TwitchLocoCommandException.class, () -> twitchConnection.sendCommand(command, stringArr));
        verify(bufferedWriter).write(anyString());
    }

    @Test
    void testSendCommandNullCommandExpectedNullPointerException() {
        assertThrows(NullPointerException.class, () -> twitchConnection.sendCommand(null, "string"));
    }

    @Test
    void testSendCommandNullArrayExpectedNullPointerException() {
        assertThrows(NullPointerException.class, () -> twitchConnection.sendCommand(Command.MESSAGE, null));
    }

    private int getCommandArgumentsLength(Command command, boolean mismatch) {
        String commandPattern = command.getPattern();
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


}
