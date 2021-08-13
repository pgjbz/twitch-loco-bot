package com.pgjbz.bot.starter.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
public class BotResponse {

    private Long id;
    private final String message;

    public BotResponse(@NonNull String message) {
        this.message = message;
    }

    public BotResponse(@NonNull Long id, @NonNull String message) {
        this(message);
        this.id = id;
    }
}
