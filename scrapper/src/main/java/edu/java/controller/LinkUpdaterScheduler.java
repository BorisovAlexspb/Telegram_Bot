package edu.java.controller;

import java.util.logging.Logger;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;


@Controller
@EnableScheduling
public class LinkUpdaterScheduler {
    Logger log = Logger.getLogger(LinkUpdaterScheduler.class.getName());

    @Scheduled(fixedDelayString = "#{@scheduler.interval}")
    public void update() {
        log.info("update");
    }
}
