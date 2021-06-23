package com.pgjbz.twitch.loco.enums;

public enum Command {
    PASS("PASS oauth:$1"),
    NICK("NICK $1"),
    JOIN("JOIN #$1"),
    MESSAGE("PRIVMSG #$1 : $2"),
    PART("PART #$1"),
    @Deprecated
    RECONNECT("RECONNECT"),
    PING("PING");

    private final String pattern;

    Command(String command) {
        this.pattern = command;
    }

    public String getPattern() {
        return pattern;
    }
}
