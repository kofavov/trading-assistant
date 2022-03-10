package ru.liga;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.liga.telegram.Bot;
import ru.liga.view.Graph;


public class Main {
    /**
     * выполняем пока не введут exit
     * <p>
     * неделя вт-сб как в csv файле и в данных ЦБРФ
     * если необходимо пн-пт то в DataHelper изменить дни недели в методе checkDayOfWeek,
     * eсли нужно выводить все дни подряд, то там же закомментировать 19-20 строчки
     */


    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
