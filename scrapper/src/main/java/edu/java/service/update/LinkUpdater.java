package edu.java.service.update;

public interface LinkUpdater {
    String GIT_HUB_LINK = "https://github.com";
    String STACK_OVER_FLOW_LINK = "https://stackoverflow.com";

    int update();

    String checkTypeOfLink(String url);
}
