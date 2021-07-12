package com.pgjbz.bot.starter.chain;

import com.pgjbz.bot.starter.util.BotUtils;
import com.pgjbz.bot.starter.service.UserService;
import com.pgjbz.twitch.loco.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import static java.util.Objects.nonNull;

@Log4j2
@RequiredArgsConstructor
public class UserChatSaveChain extends AbstractChatSaveChain {

    private final UserService userService;

    @Override
    public void doChatSave(ChatMessage chatMessage) {
        String username = chatMessage.getUser();
        if(BotUtils.isBot(username)) {
            log.info("Message from bot {}... skipping", username);
            return;
        }
        log.info("Perform user validation for user {}", username);
        try {
            userService.saveIfNotExists(username);
        } catch (Exception e) {
            log.error("Error on save user {}", username, e);
            return;
        }

        if(nonNull(next))
            next.doChatSave(chatMessage);
    }

}
