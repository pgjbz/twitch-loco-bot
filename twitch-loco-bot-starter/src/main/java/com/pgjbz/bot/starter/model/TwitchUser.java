package com.pgjbz.bot.starter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Date;

@Getter
@AllArgsConstructor
public class TwitchUser {

    private String username;
    private Date date;

    public TwitchUser(@NonNull String username) {
        this.username = username;
    }
}


