package edu.java.controller;

import edu.java.service.update.LinkUpdater;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@AllArgsConstructor
@EnableScheduling
public class LinkUpdaterScheduler {
    private final LinkUpdater linkUpdater;

    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    public void update() {
        linkUpdater.update();
    }
}
