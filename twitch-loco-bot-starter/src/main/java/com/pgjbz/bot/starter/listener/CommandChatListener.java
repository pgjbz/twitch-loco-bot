package com.pgjbz.bot.starter.listener;

import com.pgjbz.bot.starter.command.enums.Command;
import com.pgjbz.bot.starter.model.Token;
import com.pgjbz.bot.starter.model.pk.TokenPk;
import com.pgjbz.bot.starter.service.CustomCommandService;
import com.pgjbz.bot.starter.service.TokenService;
import com.pgjbz.bot.starter.util.BotUtils;
import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Objects.nonNull;

@Log4j2
@RequiredArgsConstructor
public class CommandChatListener implements LocoChatListener {

    private final TwitchConnection twitchConnection;
    private final CustomCommandService customCommandService;
    private final TokenService tokenService;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void listenChat(ChatMessage message) {
        executorService.submit(() -> {
            if(!message.getMessage().startsWith("!")
                    || executeStandardCommand(message, extractCommand(message)))
                return;
            executeCustomCommand(message);
        });
    }

    private void executeCustomCommand(ChatMessage chatMessage) {
        String command = BotUtils.extractCommandFromMessage(chatMessage).toLowerCase();
        customCommandService.findByChannelAndCommand(chatMessage.getChannel(),
                        command)
                .ifPresentOrElse(customCommand -> {
                    if(customCommand.getOnlyMods() && !twitchConnection.getModsList().contains(chatMessage.getUser())) return;
                    if(nonNull(customCommand.getTokenCost()) && customCommand.getTokenCost() > 0) {
                        log.info("Command cost {} validating if user {} on channel {} have enough tokens",
                                customCommand.getTokenCost(),
                                chatMessage.getUser(),
                                chatMessage.getChannel());
                        Optional<Token> optionalToken = tokenService.findByPk(new TokenPk(chatMessage.getUser(), chatMessage.getChannel()));
                        if(optionalToken.isEmpty()
                            || customCommand.getTokenCost() > optionalToken.get().getUnit()) {
                            log.info("User {} on channel {} don't have enough tokens to perform command {}",
                                    chatMessage.getUser(),
                                    chatMessage.getChannel(),
                                    customCommand.getCommand());
                            twitchConnection.sendMessage(String.format("@%s you don't have enough points to use this command", chatMessage.getUser()));
                            return;
                        }
                        log.info("Perform payment to use command {} for user {} on channel {}",
                                customCommand.getCommand(),
                                chatMessage.getMessage(),
                                chatMessage.getChannel());
                        Token token = optionalToken.get();
                        token.removeTokenUnit(customCommand.getTokenCost());
                        tokenService.update(token);
                    }
                    customCommand.incrementUseCount();
                    String message = BotUtils.formatMessage(chatMessage, customCommand);
                    twitchConnection.sendMessage(message);
                    customCommandService.update(customCommand);
                }, () -> log.info(
                        String.format("Non existent command [%s] for channel [%s]", command, chatMessage.getChannel())
                ));
    }

    private boolean executeStandardCommand(ChatMessage message, Command command) {
        if(nonNull(command)) {
            command.getStandardCommand().executeCommand(message, twitchConnection);
            return true;
        }
        return false;
    }

    private static Command extractCommand(ChatMessage chatMessage){
        try {
            String command =  BotUtils.extractCommandFromMessage(chatMessage);
            if(StringUtils.isNotBlank(command))
                return Command.valueOf(command.toUpperCase());
        } catch (Exception e){
            log.warn("Unknown command {}", chatMessage.getMessage());
        }
        return null;
    }
}
