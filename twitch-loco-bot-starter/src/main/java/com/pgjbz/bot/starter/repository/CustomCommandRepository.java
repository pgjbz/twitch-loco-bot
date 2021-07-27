package com.pgjbz.bot.starter.repository;

import com.pgjbz.bot.starter.model.CustomCommand;

import java.util.List;
import java.util.Optional;

public interface CustomCommandRepository  extends Repository<CustomCommand, Long> {

    boolean update(CustomCommand command);
    boolean delete(CustomCommand command);
    List<CustomCommand> findByChannel(String channel);
    Optional<CustomCommand> findByChannelAndCommand(String channel, String command);


}
