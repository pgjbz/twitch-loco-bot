package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.chain.AbstractTokenChain;
import com.pgjbz.bot.starter.chain.AddUnitTokenChain;
import com.pgjbz.bot.starter.chain.CreateUnitTokenChain;
import com.pgjbz.bot.starter.chain.UserCheckTokenChain;
import com.pgjbz.bot.starter.listener.TokenStreamListener;
import com.pgjbz.bot.starter.repository.TokenRepository;
import com.pgjbz.bot.starter.service.UserService;
import com.pgjbz.twitch.loco.listener.BotStreamInfoEventListener;

public class BotStreamInfoFactory extends AbstractBotStreamInfoFactory {

    BotStreamInfoFactory(){}

    @Override
    public BotStreamInfoEventListener createBotStreamTokenEventListener() {

        UserService userService = AbstractUserServiceFactory.getInstance().createUserService();
        TokenRepository tokenRepository = AbstractRepositoryFactory.getInstance().createTokenRepository();
        AbstractTokenChain userCheckTokenChain = new UserCheckTokenChain(userService);
        AbstractTokenChain createUnitTokenChain = new CreateUnitTokenChain(tokenRepository);
        AbstractTokenChain addUnitTokenChain = new AddUnitTokenChain(tokenRepository);

        userCheckTokenChain
                .addNext(createUnitTokenChain)
                .addNext(addUnitTokenChain);
        return new TokenStreamListener(userCheckTokenChain);
    }


}
