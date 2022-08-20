package de.pinguparty.geopingu.worker;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class of the application.
 */
@EnableRabbit
@SpringBootApplication(scanBasePackages = "de.pinguparty.geopingu.worker")
public class WorkerApp {
    public static void main(String[] args) {
        SpringApplication.run(WorkerApp.class, args);
    }
}
