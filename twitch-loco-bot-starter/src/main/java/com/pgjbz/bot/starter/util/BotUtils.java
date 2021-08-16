package com.pgjbz.bot.starter.util;

import com.pgjbz.bot.starter.configs.Configuration;
import com.pgjbz.bot.starter.model.CustomCommand;
import com.pgjbz.twitch.loco.model.ChatMessage;
import org.apache.logging.log4j.util.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class BotUtils {

    private BotUtils(){}

    public static boolean isBot(String username) {
        return Configuration.getBots().stream().anyMatch(bot -> bot.getName().equalsIgnoreCase(username));
    }

    public static String extractTarget(String message) {
        Pattern pattern = Pattern.compile("((?<=\\s)(\\w+))|((?<=@)(\\w+))");
        Matcher matcher = pattern.matcher(message);
        if(matcher.find())
            return matcher.group();
        return Strings.EMPTY;
    }

    public static String formatMessage(String command, ChatMessage chatMessage) {
        String touser = chatMessage.getUser();
        String target = extractTarget(chatMessage.getMessage());
        return command.replace("$(touser)", touser)
                .replace("$(target)", isBlank(target) ? touser : target);
    }

    public static String formatMessage(ChatMessage chatMessage, CustomCommand customCommand) {
        String baseFormat = formatMessage(customCommand.getCommandMessage(), chatMessage);
        return baseFormat.replace("$(count)", String.valueOf(customCommand.getUseCount()));
    }

    public static String extractCommandFromMessage(ChatMessage chatMessage) {
        String message = chatMessage.getMessage();
        Pattern pattern = Pattern.compile("(?<=^!)([A-Za-z]+)(?!\\S)");
        Matcher matcher = pattern.matcher(message);
        if(matcher.find())
            return matcher.group();
        return Strings.EMPTY;
    }

}
