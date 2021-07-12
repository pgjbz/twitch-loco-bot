package com.pgjbz.twitch.loco.listener.standards;

import com.pgjbz.twitch.loco.enums.CommandReceive;
import com.pgjbz.twitch.loco.model.IrcEvent;
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
        standardLocoChatListener.listenEvent(new IrcEvent(CommandReceive.JOIN, "test", "test"));
    }

}
