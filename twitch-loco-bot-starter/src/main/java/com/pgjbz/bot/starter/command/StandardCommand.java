package com.pgjbz.bot.starter.command;

import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;

public interface StandardCommand {

    void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection);

}
