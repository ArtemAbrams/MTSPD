package org.example.mtspd.service.impl;

import org.example.mtspd.service.MessageFilter;
import org.springframework.stereotype.Component;

@Component
public class LengthMessageFilter implements MessageFilter {

    private static final int MAX_LENGTH = 500;

    @Override
    public void check(String room, String nickname, String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Message must not be empty");
        }
        if (text.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("Message is too long");
        }
    }

}
