package com.pgjbz.twitch.loco.listener.standards;

import com.pgjbz.twitch.loco.model.ChatMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.spy;

public class StandardLocoChatListenerTests {

    private StandardLocoChatListener standardLocoChatListener;

    @BeforeEach
    void setup(){
        this.standardLocoChatListener = spy(StandardLocoChatListener.class);
    }

    @Test
    void testStandardLocoChatListenerExpectedSuccess() {
        standardLocoChatListener.listenChat(new ChatMessage("message", "user", "channel"));
    }

}
