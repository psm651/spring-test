package com.study.spring.module.event.service;

import com.study.spring.module.event.domain.Alarm;
import com.study.spring.module.event.event.BoardEvent;
import com.study.spring.module.event.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import static java.lang.Thread.sleep;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;

    @TransactionalEventListener()
    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    @Async
    public void alarmListener(BoardEvent boardEvent) throws InterruptedException {
        sleep(3000);
        log.info("alarmListener Thread ID : {}", Thread.currentThread().getId());
        String contents = boardEvent.getContents();
        String subject = boardEvent.getSubject();
        String writer = boardEvent.getWriter();
        Alarm alarm = new Alarm();
        alarm.setWriter(writer + "alarm");
        alarm.setContents(contents + "alarm");
        alarm.setSubject(subject + "alarm");
        alarmRepository.save(alarm);
    }
}
