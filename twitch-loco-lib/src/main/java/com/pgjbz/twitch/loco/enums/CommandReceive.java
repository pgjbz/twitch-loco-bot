package com.pgjbz.twitch.loco.enums;

import lombok.Getter;

@Getter
public enum CommandReceive {
    UNKNOWN("@@"),
    JOIN("(?<=:)(\\w+)(?=!)"),
    PART("(?<=:)(\\w+)(?=!)"),
    //check msg-id key
    USERNOTICE("(?<=display-name=)([\\w]+)"),
    CLEARCHAT("(?<=:)([\\w]+)(?!.)"),
    PING("@@"),
    PONG("@@"),
    USERSTATE("(?<=display-name=)([\\w]+)"),
    NOTICE("@@");

    private final String extractUsernameExpression;

    CommandReceive(String extractUsernameExpression) {
        this.extractUsernameExpression = extractUsernameExpression;
    }
}
