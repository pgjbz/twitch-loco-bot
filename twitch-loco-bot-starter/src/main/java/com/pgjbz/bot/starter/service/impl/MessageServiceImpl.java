package com.pgjbz.bot.starter.service.impl;

import com.pgjbz.bot.starter.model.Message;
import com.pgjbz.bot.starter.repository.MessageRepository;
import com.pgjbz.bot.starter.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;


@Log4j2
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public boolean insert(Message message) {
        boolean inserted = false;
        try {
            inserted = messageRepository.insert(message);
        } catch (Exception e) {
            log.error("Erro on insert new message {}", message.toString());
        }
        return inserted;
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }
}
