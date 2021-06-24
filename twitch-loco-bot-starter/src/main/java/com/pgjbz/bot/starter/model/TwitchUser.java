package com.pgjbz.bot.starter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor(onConstructor = @__(@Deprecated))
public class TwitchUser {

    private Long id;
    private String username;
    private Date date;

    public TwitchUser(String username) {
        this.username = username;
    }
}


