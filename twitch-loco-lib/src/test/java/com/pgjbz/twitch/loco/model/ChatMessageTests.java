package com.pgjbz.twitch.loco.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ChatMessageTests {

    @Test
    void testInstantiateWithConstructorTwitchLocoExpectedSuccess(){
        var chatMessage = new ChatMessage("message", "user", "channel");
        assertNotNull(chatMessage, "Instance of chat message cannot be null");
    }


    @ParameterizedTest
    @MethodSource(value = "illegalArgumentsChatMessage")
    void testInstantiateWithConstructorExpectedIllegalArgumentExceptionWithCustomMessage(String message, String user, String channel){
        assertThrows(IllegalArgumentException.class, () -> new ChatMessage(message, user, channel));
    }


    private static Stream<Arguments> illegalArgumentsChatMessage() {
        return Stream.of(
                Arguments.of("", "", ""),
                Arguments.of(null, null, null),
                Arguments.of("", null, ""),
                Arguments.of(null, "", null),
                Arguments.of("message", null, "channel"),
                Arguments.of(null, "username", null),
                Arguments.of(" ", " ", " "),
                Arguments.of(" ", null, null),
                Arguments.of(null, " ", " "),
                Arguments.of("", " ", "\n"),
                Arguments.of(" ", "", "\t"),
                Arguments.of("\n", "\n", "channel"),
                Arguments.of("\t", "\t", "")
        );
    }

}
