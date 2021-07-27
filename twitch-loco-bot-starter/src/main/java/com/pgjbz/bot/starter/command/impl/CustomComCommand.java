package com.pgjbz.bot.starter.command.impl;

import com.pgjbz.bot.starter.command.StandardCommand;
import com.pgjbz.bot.starter.model.CustomCommand;
import com.pgjbz.bot.starter.service.CustomCommandService;
import com.pgjbz.twitch.loco.model.ChatMessage;
import com.pgjbz.twitch.loco.network.TwitchConnection;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.util.Strings;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
@RequiredArgsConstructor
public class CustomComCommand implements StandardCommand {

    private final CustomCommandService customCommandService;

    @Override
    public void executeCommand(ChatMessage chatMessage, TwitchConnection twitchConnection) {

        log.info("Receive [createcom] commmand, processing...");
        //!createcom -t=tokens -m=0/1 only mods !command messagem
        if(!twitchConnection.getModsList().contains(chatMessage.getUser()) &&
                !twitchConnection.getChannel().equalsIgnoreCase(chatMessage.getUser())) return;

        final String message = chatMessage.getMessage();
        log.info("Validate if syntax {} is valid", message);
        if(!isValid(message)) {
            twitchConnection.sendMessage("Command pattern is invalid use !createcom -flags(-m|-t=100) !customcom message");
            return;
        }

        final String command = extractCustomCommand(message).toLowerCase();
        final String commandMessage = extractCommandMessage(message).replace("!"+command+" ", "");
        final Map<String, Integer> flags = extractFlags(message);
        final boolean onlyMods = flags.containsKey("-m=") && (flags.get("-m=") != 0);
        long tokenCost = flags.containsKey("-t=") ? flags.get("-t=") : 0L;
        customCommandService.findByChannelAndCommand(chatMessage.getChannel(), command).ifPresentOrElse(
                commandFounded -> {
                    log.info("Command {} already exists updating", commandFounded.toString());
                    if(flags.containsKey("-t="))
                        commandFounded.setTokenCost(tokenCost);
                    if(flags.containsKey("-m="))
                        commandFounded.setOnlyMods(onlyMods);
                    commandFounded.setCommandMessage(commandMessage);
                    customCommandService.update(commandFounded);
                    twitchConnection.sendMessage(String.format("Command %s updated", command));
                }, () -> {
                    final CustomCommand customCommand = new CustomCommand(null,
                            new Date(),
                            chatMessage.getUser(),
                            chatMessage.getChannel(),
                            onlyMods,
                            tokenCost,
                            command,
                            1L);
                    log.info("Command {} not exists inserting", customCommand.toString());
                    customCommand.setCommandMessage(commandMessage);
                    customCommandService.insert(customCommand);
                    twitchConnection.sendMessage(String.format("Command %s created", command));
                }
        );

    }

    private String[] extractFlagFields(String message) {
        Pattern pattern = Pattern.compile("(-[tm]=\\d+)");
        Matcher matcher = pattern.matcher(message);
        List<String> matches = new LinkedList<>();
        while(matcher.find())
            matches.add(matcher.group());
        return matches.toArray(String[]::new);
    }

    private Map<String, Integer> extractFlags(String message){
        String[] literalFlags = extractFlagFields(message);
        Map<String, Integer> flags = new HashMap<>();
        for(String literalFlag: literalFlags) {
            String flag = extractOnlyString(literalFlag);
            Integer value = extractNumbers(literalFlag);
            flags.put(flag, value);
        }
        return flags;
    }

    private Integer extractNumbers(String text) {
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(text);
        if(matcher.find())
            return Integer.parseInt(matcher.group());
        return 0;
    }

    private String extractOnlyString(String text){
        Pattern pattern = Pattern.compile("(\\D+)");
        Matcher matcher = pattern.matcher(text);
        if(matcher.find())
            return matcher.group();
        return Strings.EMPTY;
    }

    private String extractCommandMessage(String message) {
        Pattern pattern = Pattern.compile("(?<=\\s)[A-Za-z\\s\\u00C0-\\u00fc%+?$@0-9{}!'\"-/*|.~`()#^&<>]+");
        Matcher matcher = pattern.matcher(message);
        if(matcher.find())
            return matcher.group();
        return Strings.EMPTY;
    }

    private boolean isValid(String message) {
        return message.matches("^![A-Za-z]+(\\s+-[tm]=\\d+)*\\s+![A-Za-z]+\\s.+");
    }

    private String extractCustomCommand(String customCommand) {
        Pattern pattern = Pattern.compile("(?<=\\s!)([A-Za-z]+)(?=\\s)");
        Matcher matcher = pattern.matcher(customCommand);
        if(matcher.find())
            return matcher.group();
        return Strings.EMPTY;
    }
}
