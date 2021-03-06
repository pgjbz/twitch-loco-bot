package com.pgjbz.bot.starter.chain;

import com.pgjbz.bot.starter.model.Message;
import com.pgjbz.bot.starter.service.MessageService;
import com.pgjbz.twitch.loco.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import static java.util.Objects.nonNull;

@Log4j2
@RequiredArgsConstructor
public class MessageSaveChain extends AbstractChatSaveChain {

    private final MessageService messageService;

    @Override
    public void doChatSave(ChatMessage chatMessage) {

        log.info("Saving message {}", chatMessage.toString());

        try {
            Message message = new Message(chatMessage);
            messageService.insert(message);
        } catch (Exception e) {
            log.error("Error on save message {}", chatMessage.toString(), e);
            return;
        }
        if(nonNull(next))
            next.doChatSave(chatMessage);
    }
}
