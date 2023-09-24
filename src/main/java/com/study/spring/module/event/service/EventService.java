package com.study.spring.module.event.service;

import com.study.spring.module.event.domain.Board;
import com.study.spring.module.event.event.BoardEvent;
import com.study.spring.module.event.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class EventService {

    private final BoardRepository boardRepository;
    //    private final AlarmRepository alarmRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void saveBoard(String value) {
        log.info("EventService Thread ID : {}", Thread.currentThread().getId());

        String subject = value + " - 제목이다";
        String contents = value + " - 이게 내용이다";
        String writer = value + " - 작성자!!이다";
        Board board = new Board();
        board.setContents(contents);
        board.setSubject(subject);
        board.setWriter(writer);


        boardRepository.save(board);
        applicationEventPublisher.publishEvent(new BoardEvent(subject, contents, writer));
//        alarmRepository.save(alarm);
    }
}
