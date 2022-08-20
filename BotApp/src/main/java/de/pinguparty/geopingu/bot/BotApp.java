package de.pinguparty.geopingu.bot;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class of the application.
 */
@EnableRabbit
@SpringBootApplication(scanBasePackages = "de.pinguparty.geopingu.bot")
public class BotApp {

    public static void main(String[] args) {
        SpringApplication.run(BotApp.class, args);
    }

}
