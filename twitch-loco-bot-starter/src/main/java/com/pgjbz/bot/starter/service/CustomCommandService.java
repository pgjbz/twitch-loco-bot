package com.pgjbz.bot.starter.service;

import com.pgjbz.bot.starter.model.CustomCommand;

import java.util.Optional;

public interface CustomCommandService extends Service<CustomCommand, Long> {

    Optional<CustomCommand> findByChannelAndCommand(String channel, String command);
    boolean update(CustomCommand customCommand);
}
