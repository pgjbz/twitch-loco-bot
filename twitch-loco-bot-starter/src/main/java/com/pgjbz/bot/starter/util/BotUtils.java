package com.pgjbz.bot.starter.util;

import com.pgjbz.bot.starter.configs.Configuration;
import org.apache.logging.log4j.util.Strings;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

}
