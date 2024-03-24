package edu.java.controller;

import edu.java.service.update.LinkUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final LinkUpdater linkUpdater;

    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    public void update() {
        linkUpdater.update();
    }
}
