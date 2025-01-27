package com.example.surgeonmodel;

import com.example.surgeonmodel.bot.SurgeonBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class SurgeonModelApplication {

    public static void main(String[] args) {
        SpringApplication.run(SurgeonModelApplication.class, args);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new SurgeonBot());
            System.out.println("Telegram бот успешно запущен!");
        } catch (TelegramApiException e) {
            System.err.println("Ошибка при запуске Telegram бота: " + e.getMessage());
        }
    }
}