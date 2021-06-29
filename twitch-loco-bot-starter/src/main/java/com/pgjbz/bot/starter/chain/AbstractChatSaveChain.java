package com.pgjbz.bot.starter.chain;

import com.pgjbz.twitch.loco.model.ChatMessage;

import static java.util.Objects.isNull;

public abstract class AbstractChatSaveChain {

    protected AbstractChatSaveChain next;

    public AbstractChatSaveChain addNext(AbstractChatSaveChain nextStep) {
        if(isNull(next))
            return next = nextStep;
        return next.addNext(nextStep);
    }

    public abstract void doChatSave(ChatMessage chatMessage);

}
