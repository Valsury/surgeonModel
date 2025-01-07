package com.example.surgeonmodel.bot;

import com.example.surgeonmodel.model.Appointment;
import com.example.surgeonmodel.model.AppointmentDTO;
import com.example.surgeonmodel.model.Patient;
import com.example.surgeonmodel.model.Surgeon;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SurgeonBot extends TelegramLongPollingBot {

    private static final String BOT_TOKEN = "7958734772:AAGYLgmH9Z1Rb2hwl9xRzcqw1xLpxghQ-Z4"; // Замените на ваш токен
    private static final String BOT_USERNAME = "BossOfAllSurgeons";
    private static final String API_URL = "http://localhost:8080/api";

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()) // Добавляем поддержку Java 8 Date/Time API
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // Отключаем запись дат как таймстампов
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd, HH:mm");

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
                case "/start":
                    sendMessage(chatId, "Привет! Используй команды:\n" +
                            "/surgeons - список хирургов\n" +
                            "/patients - список пациентов\n" +
                            "/appointments - список приемов");
                    break;
                case "/surgeons":
                    sendSurgeonsList(chatId);
                    break;
                case "/patients":
                    sendPatientsList(chatId);
                    break;
                case "/appointments":
                    sendAppointmentsList(chatId);
                    break;
                default:
                    sendMessage(chatId, "Неизвестная команда. Используй /start для списка команд.");
            }
        }
    }

    private void sendSurgeonsList(long chatId) {
        try {
            String json = sendGetRequest(API_URL + "/surgeons");
            List<Surgeon> surgeons = objectMapper.readValue(json, new TypeReference<>() {});

            StringBuilder message = new StringBuilder("Хирурги:\n");
            for (Surgeon surgeon : surgeons) {
                message.append(surgeon.getFirstName()).append(" ").append(surgeon.getLastName()).append("\n");
            }
            sendMessage(chatId, message.toString());
        } catch (IOException e) {
            sendMessage(chatId, "Ошибка при получении данных о хирургах.");
            e.printStackTrace();
        }
    }

    private void sendPatientsList(long chatId) {
        try {
            String json = sendGetRequest(API_URL + "/patients");
            List<Patient> patients = objectMapper.readValue(json, new TypeReference<>() {});

            StringBuilder message = new StringBuilder("Пациенты:\n");
            for (Patient patient : patients) {
                message.append(patient.getFirstName()).append(" ").append(patient.getLastName()).append("\n");
            }
            sendMessage(chatId, message.toString());
        } catch (IOException e) {
            sendMessage(chatId, "Ошибка при получении данных о пациентах.");
            e.printStackTrace();
        }
    }

    private void sendAppointmentsList(long chatId) {
        try {
            String json = sendGetRequest(API_URL + "/appointments");
            List<AppointmentDTO> appointments = objectMapper.readValue(json, new TypeReference<>() {});

            StringBuilder message = new StringBuilder("Приемы:\n");
            for (AppointmentDTO appointment : appointments) {
                String formattedDateTime = appointment.getAppointmentDateTime() != null
                        ? appointment.getAppointmentDateTime().format(DATE_TIME_FORMATTER) // Форматируем дату и время
                        : "Не указано";

                message.append("Дата и время: ")
                        .append(formattedDateTime) // Используем отформатированную дату
                        .append("\nТип приема: ")
                        .append(appointment.getAppointmentType())
                        .append("\nХирург: ")
                        .append(appointment.getSurgeonName())
                        .append("\nПациент: ")
                        .append(appointment.getPatientName())
                        .append("\n\n"); // Разделитель между приемами
            }
            sendMessage(chatId, message.toString());
        } catch (IOException e) {
            sendMessage(chatId, "Ошибка при получении данных о приемах.");
            e.printStackTrace();
        }
    }

    private String sendGetRequest(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Ошибка при запросе: " + response.code());
            }
            return response.body().string();
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }
}