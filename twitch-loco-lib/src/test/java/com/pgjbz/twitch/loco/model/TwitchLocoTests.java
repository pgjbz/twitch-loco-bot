package com.pgjbz.twitch.loco.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TwitchLocoTests {

    @Test
    void testInstantiateWithConstructorTwitchLocoExpectedSuccess(){
        var twitchLoco = new TwitchLoco("twitch", "loco");
        assertNotNull(twitchLoco, "Instance of twitch loco cannot be null");
    }

    @Test
    void testInstantiateWithBuilderTwitchLocoExpectedSuccess(){
        var twitchLoco = TwitchLoco.builder().username("twitch").oauth("loco");
        assertNotNull(twitchLoco, "Instance of twitch loco cannot be null");
    }

    @ParameterizedTest
    @MethodSource(value = "illegalArgumentsTwitchLocoBot")
    void testInstantiateWithConstructorExpectedIllegalArgumentExceptionWithCustomMessage(String username, String oauth){
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> new TwitchLoco(username, oauth));
        assertEquals(illegalArgumentException.getMessage(), "Username and oauth are mandatory");
    }

    @ParameterizedTest
    @MethodSource(value = "illegalArgumentsTwitchLocoBot")
    void testInstantiateWithBuilderExpectedIllegalArgumentExceptionWithCustomMessage(String username, String oauth){
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> TwitchLoco
                        .builder()
                        .username(username)
                        .oauth(oauth)
                        .build());
        assertEquals(illegalArgumentException.getMessage(), "Username and oauth are mandatory");
    }

    private static Stream<Arguments> illegalArgumentsTwitchLocoBot() {
        return Stream.of(
                Arguments.of("", ""),
                Arguments.of(null, null),
                Arguments.of("", null),
                Arguments.of(null, ""),
                Arguments.of("twitch", null),
                Arguments.of(null, "loco"),
                Arguments.of(" ", " "),
                Arguments.of(" ", null),
                Arguments.of(null, " "),
                Arguments.of("", " "),
                Arguments.of(" ", ""),
                Arguments.of("\n", "\n"),
                Arguments.of("\t", "\t")
        );
    }

}
