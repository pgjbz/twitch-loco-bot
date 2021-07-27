package com.pgjbz.bot.starter.service;

import com.pgjbz.bot.starter.model.CustomCommand;

import java.util.List;
import java.util.Optional;

public interface CustomCommandService extends Service<CustomCommand, Long> {

    Optional<CustomCommand> findByChannelAndCommand(String channel, String command);
    boolean update(CustomCommand customCommand);
    List<CustomCommand> findByChannel(String channel);
    boolean delete(CustomCommand customCommand);
}
