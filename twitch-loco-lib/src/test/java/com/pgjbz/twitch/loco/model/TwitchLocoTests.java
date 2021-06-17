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
        var twitchLoco = new TwitchLoco("twitch", "loco", "channel");
        assertNotNull(twitchLoco, "Instance of twitch loco cannot be null");
    }

    @Test
    void testInstantiateWithBuilderTwitchLocoExpectedSuccess(){
        var twitchLoco = TwitchLoco.builder().username("twitch").oauth("loco").channel("channel").build();
        assertNotNull(twitchLoco, "Instance of twitch loco cannot be null");
    }

    @ParameterizedTest
    @MethodSource(value = "illegalArgumentsTwitchLocoBot")
    void testInstantiateWithConstructorExpectedIllegalArgumentExceptionWithCustomMessage(String username, String oauth, String channel){
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> new TwitchLoco(username, oauth, channel));
        assertEquals(illegalArgumentException.getMessage(), "Username, oauth and channel are mandatory");
    }

    @ParameterizedTest
    @MethodSource(value = "illegalArgumentsTwitchLocoBot")
    void testInstantiateWithBuilderExpectedIllegalArgumentExceptionWithCustomMessage(String username, String oauth, String channel){
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> TwitchLoco
                        .builder()
                        .username(username)
                        .oauth(oauth)
                        .channel(channel)
                        .build());
        assertEquals(illegalArgumentException.getMessage(), "Username, oauth and channel are mandatory");
    }

    private static Stream<Arguments> illegalArgumentsTwitchLocoBot() {
        return Stream.of(
                Arguments.of("", "", ""),
                Arguments.of(null, null, null),
                Arguments.of("", null, ""),
                Arguments.of(null, "", null),
                Arguments.of("twitch", null, "channel"),
                Arguments.of(null, "loco", null),
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
