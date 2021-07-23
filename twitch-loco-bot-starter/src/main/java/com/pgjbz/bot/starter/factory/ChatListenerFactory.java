package com.pgjbz.bot.starter.factory;

import com.pgjbz.bot.starter.chain.AbstractChatSaveChain;
import com.pgjbz.bot.starter.chain.MessageSaveChain;
import com.pgjbz.bot.starter.chain.UserChatSaveChain;
import com.pgjbz.bot.starter.listener.CommandChatListener;
import com.pgjbz.bot.starter.listener.SaveChatListener;
import com.pgjbz.bot.starter.repository.MessageRepository;
import com.pgjbz.bot.starter.service.UserService;
import com.pgjbz.twitch.loco.listener.LocoChatListener;
import com.pgjbz.twitch.loco.network.TwitchConnection;

public class ChatListenerFactory extends AbstractChatListenerFactory {

    ChatListenerFactory(){}

    @Override
    public LocoChatListener createCommandChatListener(TwitchConnection twitchConnection) {
        return new CommandChatListener(twitchConnection);
    }

    @Override
    public LocoChatListener createChatSaveLister() {
        UserService userService = AbstractUserServiceFactory.getInstance().createUserService();
        MessageRepository messageRepository = AbstractRepositoryFactory.getInstance().createMessageRepository();
        AbstractChatSaveChain userChatSaveChain = new UserChatSaveChain(userService);
        AbstractChatSaveChain messageChatSaveChain = new MessageSaveChain(messageRepository);
        userChatSaveChain.addNext(messageChatSaveChain);
        return new SaveChatListener(userChatSaveChain);
    }
}
