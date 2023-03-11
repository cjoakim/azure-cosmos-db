package org.cjoakim.cosmosdb.cassandra;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public String getGreeting() {
        return "org.cjoakim.cosmosdb.cassandra";
    }

    public static void main(String[] args) {

        log.warn(new App().getGreeting());
    }
}
