package com.pgjbz.bot.starter.command.enums;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.bot.starter.command.impl.*;
import com.pgjbz.bot.starter.factory.AbstractRepositoryFactory;
import lombok.Getter;

@Getter
public enum Command {
    TOKENS(new TokensCommand(AbstractRepositoryFactory.getInstance().createTokenRepository())),
    DICE(new DiceCommand(AbstractRepositoryFactory.getInstance().createTokenRepository())),
    JOKE(new JokeCommand(AbstractRepositoryFactory.getInstance().createJokeRepository())),
    FLIRT(new FlirtCommand(AbstractRepositoryFactory.getInstance().createTeaserRepository())),
    STEAL(new StealCommand(AbstractRepositoryFactory.getInstance().createStealRepository())),
    COMMANDS(new CommandsCommand());

    private final StandardCommand standardCommand;

    Command(StandardCommand standardCommand) {
        this.standardCommand = standardCommand;
    }
}
