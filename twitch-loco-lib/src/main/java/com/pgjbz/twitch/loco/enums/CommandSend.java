package com.pgjbz.twitch.loco.enums;

public enum CommandSend {
    PASS("PASS oauth:$1"),
    NICK("NICK $1"),
    JOIN("JOIN #$1"),
    MESSAGE("PRIVMSG #$1 :$2"),
    PART("PART #$1"),
    @Deprecated
    RECONNECT("RECONNECT"),
    PING("PING"),
    PONG("PONG :tmi.twitch.tv"),
    CAP_COMMANDS("CAP REQ :twitch.tv/commands"),
    CAP_MEMBERSHIP("CAP REQ :twitch.tv/membership"),
    CAP_TAG("CAP REQ :twitch.tv/tags");

    private final String pattern;

    CommandSend(String command) {
        this.pattern = command;
    }

    public String getPattern() {
        return pattern;
    }
}
