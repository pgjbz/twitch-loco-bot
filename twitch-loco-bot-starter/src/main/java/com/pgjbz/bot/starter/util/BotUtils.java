package com.pgjbz.bot.starter.util;

import com.pgjbz.bot.starter.configs.Configuration;

public class BotUtils {

    private BotUtils(){}

    public static boolean isBot(String username) {
        return Configuration.getBots().stream().anyMatch(bot -> bot.getName().equalsIgnoreCase(username));
    }

}
