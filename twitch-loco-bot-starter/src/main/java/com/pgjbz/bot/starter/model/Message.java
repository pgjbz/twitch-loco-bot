package com.pgjbz.bot.starter.model;

import com.pgjbz.twitch.loco.util.CheckUtil;

import java.util.Date;

import static com.pgjbz.twitch.loco.util.CheckUtil.requireNotBlank;

public record Message (
        Long id,
        String message,
        String user,
        String channel,
        Date messageDate){


    public Message {
        requireNotBlank(message, "Message is mandatory");
        requireNotBlank(user, "User is mandatory");
        requireNotBlank(channel, "Channel is mandatory");
        CheckUtil.requireNonNull(messageDate, "Message date is mandatory");
    }
}
