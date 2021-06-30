package com.pgjbz.twitch.loco.listeners.standards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.spy;

public class StandardLocoIrcEventListenerTests {

    private StandardLocoIrcEventsListener standardLocoChatListener;

    @BeforeEach
    void setup(){
        this.standardLocoChatListener = spy(StandardLocoIrcEventsListener.class);
    }

    @Test
    void testStandardLocoChatListenerExpectedSuccess() {
        standardLocoChatListener.listenEvent("string");
    }

}
