package edu.java.configuration;

import edu.java.controller.LinkUpdaterScheduler;
import edu.java.service.update.LinkUpdater;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SchedulerConfiguration {

    private final LinkUpdater linkUpdater;

    @Bean
    public LinkUpdaterScheduler linkUpdaterScheduler() {
        return new LinkUpdaterScheduler(linkUpdater);
    }
}
