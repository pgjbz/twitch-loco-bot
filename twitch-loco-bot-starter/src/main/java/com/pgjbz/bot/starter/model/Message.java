package com.pgjbz.bot.starter.model;

import lombok.Getter;
import lombok.NonNull;

import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Getter
public class Message {

    private Long id;
    private final String username;
    private final String message;
    private Date messageDate;

    public Message(String username, String message) {
        if(isBlank(username) || isBlank(message))
            throw new IllegalArgumentException("Username and message are mandatory");
        this.username = username;
        this.message = message;
    }

    public Message(@NonNull Long id, String username, String message, @NonNull Date messageDate) {
        this(username, message);
        this.id = id;
        this.messageDate = messageDate;
    }
}
