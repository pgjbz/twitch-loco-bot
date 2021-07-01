package com.pgjbz.bot.starter.chain;

import com.pgjbz.bot.starter.model.Token;

import static java.util.Objects.isNull;

public abstract class AbstractTokenChain {

    protected AbstractTokenChain next;

    public AbstractTokenChain addNext(AbstractTokenChain nextStep) {
        if(isNull(next))
            return next = nextStep;
        return next.addNext(nextStep);
    }

    public abstract void doAddUnits(Token token);

}
