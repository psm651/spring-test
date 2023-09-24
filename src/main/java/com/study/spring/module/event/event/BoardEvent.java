package com.study.spring.module.event.event;

import lombok.Getter;

@Getter
public class BoardEvent {
    private final String subject;
    private final String contents;
    private final String writer;

    public BoardEvent(String subject, String contents, String writer) {
        this.subject = subject;
        this.contents = contents;
        this.writer = writer;
    }
}
