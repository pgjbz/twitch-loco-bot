package com.pgjbz.bot.starter.model;

import com.pgjbz.twitch.loco.model.ChatMessage;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Date;

@Getter
@ToString
public class Message extends ChatMessage {

    private Long id;
    private Date messageDate;

    public Message(@NonNull ChatMessage chatMessage) {
        super(chatMessage.getMessage(), chatMessage.getUser(), chatMessage.getChannel());
    }

    public Message(@NonNull Long id, String message, String user, String channel, @NonNull Date messageDate) {
        super(message, user, channel);
        this.id = id;
        this.messageDate = messageDate;
    }
}
