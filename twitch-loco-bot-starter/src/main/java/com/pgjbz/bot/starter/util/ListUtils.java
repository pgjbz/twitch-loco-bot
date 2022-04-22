package com.pgjbz.bot.starter.util;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {
    private ListUtils() {
    }

    public static <T> List<List<T>> split(List<T> list, final int L) {
        List<List<T>> parts = new ArrayList<>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<>(
                list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;
    }
}
