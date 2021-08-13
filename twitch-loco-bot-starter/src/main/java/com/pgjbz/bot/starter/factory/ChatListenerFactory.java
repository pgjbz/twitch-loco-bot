package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.chain.AbstractChatSaveChain;
import com.pgjbz.bot.starter.chain.MessageSaveChain;
import com.pgjbz.bot.starter.chain.UserChatSaveChain;
import com.pgjbz.bot.starter.listener.BotTargetChatListener;
import com.pgjbz.bot.starter.listener.CommandChatListener;
import com.pgjbz.bot.starter.listener.SaveChatListener;
import com.pgjbz.bot.starter.service.BotResponseService;
import com.pgjbz.bot.starter.service.MessageService;
import com.pgjbz.bot.starter.service.UserService;
import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.network.TwitchConnection;

public class ChatListenerFactory extends AbstractChatListenerFactory {

    ChatListenerFactory(){}

    @Override
    public LocoChatListener createCommandChatListener(TwitchConnection twitchConnection) {
        return new CommandChatListener(twitchConnection,
                AbstractServiceFactory.getInstance().createCustomCommandService(),
                AbstractServiceFactory.getInstance().createTokenService());
    }

    @Override
    public LocoChatListener createBotTargetChatListener(TwitchConnection twitchConnection) {
        BotResponseService botResponseService = AbstractServiceFactory.getInstance().createBotResponseServiceService();
        return new BotTargetChatListener(twitchConnection, botResponseService);
    }

    @Override
    public LocoChatListener createChatSaveLister() {
        MessageService messageService = AbstractServiceFactory.getInstance().createMessageService();
        UserService userService = AbstractServiceFactory.getInstance().createUserService();
        AbstractChatSaveChain userChatSaveChain = new UserChatSaveChain(userService);
        AbstractChatSaveChain messageChatSaveChain = new MessageSaveChain(messageService);
        userChatSaveChain.addNext(messageChatSaveChain);
        return new SaveChatListener(userChatSaveChain);
    }
}
