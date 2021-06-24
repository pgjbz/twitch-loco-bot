package com.pgjbz.bot.starter.repository;

import java.util.List;

public interface Repository <T, P> {

    boolean insert(T t);
    List<T> findAll();

}
