package com.pgjbz.bot.starter.chain;

import com.pgjbz.twitch.loco.model.IrcEvent;

import static java.util.Objects.isNull;

public abstract class AbstractIrcEventSaveChain {

    protected AbstractIrcEventSaveChain  nextStep;

    public AbstractIrcEventSaveChain addNext(AbstractIrcEventSaveChain nextStep) {
        if(isNull(this.nextStep))
            return this.nextStep = nextStep;
        return nextStep.addNext(nextStep);
    }

    public abstract void doSaveIrcEvent(IrcEvent ircEvent);
}
