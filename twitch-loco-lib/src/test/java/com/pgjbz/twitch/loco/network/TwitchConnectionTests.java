package com.pgjbz.twitch.loco.network;

import com.pgjbz.twitch.loco.exception.TwitchLocoConnectionException;
import com.pgjbz.twitch.loco.model.TwitchLoco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TwitchConnectionTests {

    private TwitchLoco twitchLoco;

    @BeforeEach
    void setup() {
        twitchLoco = new TwitchLoco("username", "oauth", "channel");
    }

    @Test
    void testGetConnectionExpectedTwitchLocoConnectionExceptionWhenCreateSocketConnection() throws Exception {
        try (MockedConstruction<Socket> mockedSocket = mockConstruction(Socket.class, (mocked, ctx) ->
                doThrow(IOException.class).when(mocked).connect(any(SocketAddress.class)))) {
            assertThrows(TwitchLocoConnectionException.class, () -> TwitchConnection.getConnection(twitchLoco));
            Socket socket = mockedSocket.constructed().get(0);
            verify(socket).connect(any(SocketAddress.class));
        }
    }

    @Test
    void testGetConnectionExpectedTwitchLocoConnectionExceptionWhenCreateBufferedWriter() throws Exception {
        try (MockedConstruction<Socket> mockedSocket = mockConstruction(Socket.class, (mocked, ctx) ->
                doThrow(IOException.class).when(mocked).getOutputStream())) {
            assertThrows(TwitchLocoConnectionException.class, () -> TwitchConnection.getConnection(twitchLoco));
            Socket socket = mockedSocket.constructed().get(0);
            verify(socket).connect(any(SocketAddress.class));
            verify(socket).getOutputStream();
        }
    }

    @Test
    void testGetConnectionExpectedTwitchLocoConnectionExceptionWhenCreateInputStreamReader() throws Exception {
        try (MockedConstruction<Socket> mockedSocket = mockConstruction(Socket.class, (mocked, ctx) -> {
            doReturn(mock(OutputStream.class)).when(mocked).getOutputStream();
            doThrow(IOException.class).when(mocked).getInputStream();
        })) {
            assertThrows(TwitchLocoConnectionException.class, () -> TwitchConnection.getConnection(twitchLoco));
            Socket socket = mockedSocket.constructed().get(0);
            verify(socket).connect(any(SocketAddress.class));
            verify(socket).getOutputStream();
            verify(socket).getInputStream();
        }
    }

    @Test
    void testGetConnectionExpectedSuccess() throws Exception {
        try (
                MockedConstruction<Socket> mockedSocket = mockConstruction(Socket.class, (mocked, ctx) -> {
                    doReturn(mock(OutputStream.class)).when(mocked).getOutputStream();
                    doReturn(mock(InputStream.class)).when(mocked).getInputStream();
                    doReturn(3).when(mocked).getReceiveBufferSize();
                    doReturn(true).when(mocked).isConnected();
                })) {
            var botConnection = TwitchConnection.getConnection(twitchLoco);
            Socket socket = mockedSocket.constructed().get(0);
            assertNotNull(botConnection, "Bot connection cannot be null");
            verify(socket).connect(any(SocketAddress.class));
            verify(socket).getOutputStream();
            verify(socket).getInputStream();
        }
    }
}
