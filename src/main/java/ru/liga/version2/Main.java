package ru.liga.version2;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.liga.version2.model.DataTable;
import ru.liga.version2.model.FullMoonCalendar;
import ru.liga.version2.telegram.Bot;

@Slf4j
public class Main {
    public static void main(String[] args) {
        if (DataTable.init() && FullMoonCalendar.init()) {
            try {
                TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                botsApi.registerBot(new Bot());
            } catch (TelegramApiException e) {
                log.info(e.toString());
            }
        }
    }
}
