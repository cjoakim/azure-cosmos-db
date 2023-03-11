package org.cjoakim.cosmosdb;

import lombok.extern.slf4j.Slf4j;

/**
 * This class is the entry point for the application.
 *
 * Chris Joakim, Microsoft
 */
@Slf4j
public class App {
    public String getGreeting() {

        return "org.cjoakim.cosmosdb";
    }

    public static void main(String[] args) {

        log.warn(new App().getGreeting());
    }
}
