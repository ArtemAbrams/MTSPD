package org.example.mtspd.service;

public interface MessageFilter {

    void check(String room, String nickname, String text);

}
