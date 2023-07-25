package ru.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StatsServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(StatsServiceApp.class, args);
        /*SpringApplication app = new SpringApplication(StatsServiceApp.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "9090"));
        app.run(args);*/
    }
}
