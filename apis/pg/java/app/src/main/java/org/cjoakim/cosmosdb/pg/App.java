package org.cjoakim.cosmosdb.pg;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public String getGreeting() {
        return "org.cjoakim.cosmosdb.pg";
    }

    public static void main(String[] args) {

        log.warn(new App().getGreeting());
    }
}
