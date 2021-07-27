package com.pgjbz.bot.starter.service.impl;

import com.pgjbz.bot.starter.model.CustomCommand;
import com.pgjbz.bot.starter.repository.CustomCommandRepository;
import com.pgjbz.bot.starter.service.CustomCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@RequiredArgsConstructor
public class CustomCommandServiceImpl implements CustomCommandService {

    private final CustomCommandRepository customCommandRepository;

    @Override
    public boolean insert(CustomCommand command) {
        log.info("Inserting new command {}", command.toString());
        boolean inserted = false;
        try {
            inserted = customCommandRepository.insert(command);
        } catch (Exception e) {
            log.error("Error on insert command {}", command.toString());
        }
        return inserted;
    }

    @Override
    public List<CustomCommand> findAll() {
        try {
            return customCommandRepository.findAll();
        } catch (Exception e) {
            log.error("Error on find all commmmands");
        }
        return new ArrayList<>();
    }

    @Override
    public Optional<CustomCommand> findByChannelAndCommand(String channel, String command) {
        try {
            return customCommandRepository.findByChannelAndCommand(channel, command);
        } catch (Exception e) {
            log.error("Error on find command {} on channel {}", command, channel, e);
        }
        return Optional.empty();
    }

    @Override
    public boolean update(CustomCommand customCommand) {
        log.info("Updating command {}", customCommand.toString());
        try {
            return customCommandRepository.update(customCommand);
        } catch (Exception e){
            log.error("Error on update command {}", customCommand.toString());
        }
        return false;
    }
}
