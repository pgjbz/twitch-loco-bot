package com.pgjbz.bot.starter.command.enums;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.bot.starter.command.impl.*;
import com.pgjbz.bot.starter.factory.AbstractServiceFactory;
import lombok.Getter;

@Getter
public enum Command {
    TOKENS(new TokensCommand(AbstractServiceFactory.getInstance().createTokenService())),
    DICE(new DiceCommand(AbstractServiceFactory.getInstance().createTokenService())),
    JOKE(new JokeCommand(AbstractServiceFactory.getInstance().createJokeService())),
    FLIRT(new FlirtCommand(AbstractServiceFactory.getInstance().createFlirtService())),
    STEAL(new StealCommand(AbstractServiceFactory.getInstance().createStealService())),
    COMMANDS(new CommandsCommand(AbstractServiceFactory.getInstance().createCustomCommandService())),
    BOT(new BotCommand()),
    CUSTOMCOM(new CustomComCommand(AbstractServiceFactory.getInstance().createCustomCommandService())),
    DELCUSTOMCOM(new DelCustomComCommand(AbstractServiceFactory.getInstance().createCustomCommandService())),
    RANKING(new RankingCommand(AbstractServiceFactory.getInstance().createRankingService())),
    UPTIME(new UptimeCommand()),
    FOLLOWAGE(new FollowAgeCommand());

    private final StandardCommand standardCommand;

    Command(StandardCommand standardCommand) {
        this.standardCommand = standardCommand;
    }
}
