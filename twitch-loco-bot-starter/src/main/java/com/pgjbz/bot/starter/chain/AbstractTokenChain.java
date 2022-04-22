package com.pgjbz.bot.starter.chain;

import static java.util.Objects.isNull;

import java.util.List;

import com.pgjbz.bot.starter.model.Token;

public abstract class AbstractTokenChain {

    protected AbstractTokenChain next;

    public AbstractTokenChain addNext(AbstractTokenChain nextStep) {
        if(isNull(next))
            return next = nextStep;
        return next.addNext(nextStep);
    }

    public abstract void doAddUnits(List<Token> token);

}
