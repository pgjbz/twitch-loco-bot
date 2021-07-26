package com.pgjbz.bot.starter.service;

import java.util.List;

public interface Service<T, P> {

    boolean insert(T t);
    List<T> findAll();

}
