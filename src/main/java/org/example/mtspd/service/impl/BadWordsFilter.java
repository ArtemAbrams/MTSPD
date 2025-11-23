package org.example.mtspd.service.impl;

import org.example.mtspd.service.MessageFilter;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class BadWordsFilter implements MessageFilter {

    private static final Set<String> BAD_WORDS = Set.of("Donald Trump", "Palestine");

    @Override
    public void check(String room, String nickname, String text) {
        String lower = text.toLowerCase();
        boolean contains = BAD_WORDS.stream().anyMatch(lower::contains);
        if (contains) {
            throw new IllegalArgumentException("Message contains forbidden words");
        }
    }

}
