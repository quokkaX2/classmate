package com.quokka.classmate.event;

import com.quokka.classmate.service.QueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@RequiredArgsConstructor
public class EventScheduler {
    private final QueueService queueService;

    @Scheduled(fixedDelay = 3000)
    public void periodicQueue() {
        queueService.process();
    }
}
