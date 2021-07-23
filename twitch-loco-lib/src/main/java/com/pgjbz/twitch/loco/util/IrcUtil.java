package com.pgjbz.twitch.loco.util;

import com.pgjbz.twitch.loco.enums.CommandReceive;
import com.pgjbz.twitch.loco.model.IrcEvent;
import lombok.extern.log4j.Log4j2;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Log4j2
public class IrcUtil {

    private IrcUtil() {
    }

    public static String ircEventNameExtract(String ircEventString) {
        Pattern pattern = Pattern.compile("((?<=\\s)([A-Z]+))|(([A-Z]+)(?=\\s))");
        Matcher matcher = pattern.matcher(ircEventString);
        if (matcher.find())
            return matcher.group();
        return "UNKNOWN";
    }

    public static String extractChannel(String ircEventString) {
        Pattern pattern = Pattern.compile("(?<=\\s#)(\\w+)");
        Matcher matcher = pattern.matcher(ircEventString);
        if (matcher.find())
            return matcher.group();
        return "UNKNOWN";
    }

    public static String extractUsername(String ircEventString, CommandReceive commandReceive) {
        Pattern pattern = Pattern.compile(commandReceive.getExtractUsernameExpression());
        Matcher matcher = pattern.matcher(ircEventString);
        if (matcher.find())
            return matcher.group();
        return "UNKNOWN";
    }

    public static IrcEvent extractEvent(String ircEventFullString) {
        IrcEvent ircEvent = null;
        try {
            CommandReceive commandReceive = getCommandReceive(ircEventFullString);
            switch (commandReceive) {
                case PING:
                case UNKNOWN:
                case NOTICE:
                    ircEvent = new IrcEvent(commandReceive, extractUsername(ircEventFullString, commandReceive), extractChannel(ircEventFullString));
                    ircEvent.setUnknownString(ircEventFullString);
                    break;
                default:
                    String channel = extractChannel(ircEventFullString);
                    String username = extractUsername(ircEventFullString, commandReceive);
                    ircEvent = new IrcEvent(commandReceive, username, channel);
                    int indexReplace = ircEventFullString.indexOf(":tmi.twitch.tv");
                    if (indexReplace >= 6) {
                        String ircKeysString = ircEventFullString.substring(0, indexReplace - 1);
                        if (isNotBlank(ircKeysString)) {
                            String[] fields = ircKeysString.split(";");
                            for (String field : fields) {
                                String[] keyValue = field.split("=");
                                if (keyValue.length < 2) continue;
                                ircEvent.getKeys().put(keyValue[0], keyValue[1]);
                            }
                        }
                    }
            }
        } catch (Exception e) {
            log.error("Error on extract event {}", e.getMessage(), e);
        }
        return ircEvent;
    }

    private static CommandReceive getCommandReceive(String ircEventFullString) {
        try {
            return CommandReceive.valueOf(ircEventNameExtract(ircEventFullString));
        } catch (Exception e) {
            return CommandReceive.UNKNOWN;
        }
    }

}
