package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.chain.AbstractTokenChain;
import com.pgjbz.bot.starter.chain.AddUnitTokenChain;
import com.pgjbz.bot.starter.chain.CreateUnitTokenChain;
import com.pgjbz.bot.starter.chain.UserCheckTokenChain;
import com.pgjbz.bot.starter.listener.stream.TokenStreamListener;
import com.pgjbz.bot.starter.service.TokenService;
import com.pgjbz.bot.starter.service.UserService;
import com.pgjbz.twitch.loco.listener.BotStreamInfoEventListener;

public class BotStreamInfoFactory extends AbstractBotStreamInfoFactory {

    BotStreamInfoFactory(){}

    @Override
    public BotStreamInfoEventListener createBotStreamTokenEventListener() {

        UserService userService = AbstractServiceFactory.getInstance().createUserService();
        TokenService tokenService = AbstractServiceFactory.getInstance().createTokenService();
        AbstractTokenChain userCheckTokenChain = new UserCheckTokenChain(userService);
        AbstractTokenChain createUnitTokenChain = new CreateUnitTokenChain(tokenService);
        AbstractTokenChain addUnitTokenChain = new AddUnitTokenChain(tokenService);

        userCheckTokenChain
                .addNext(createUnitTokenChain)
                .addNext(addUnitTokenChain);
        return new TokenStreamListener(userCheckTokenChain);
    }


}
