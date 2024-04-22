package com.quokka.classmate.event;

import com.quokka.classmate.service.QueueService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EventScheduler {
    private final QueueService queueService;

//    @PostConstruct
//    public void init() {
//        periodicQueue(); // 초기화 시 스케줄러 메서드를 한 번 실행하도록 호출
//    }
//
//    @Scheduled(fixedDelay = 10000)
//    public void periodicQueue() {
//        log.info("스케쥴러");
//        queueService.process();
//    }
}
