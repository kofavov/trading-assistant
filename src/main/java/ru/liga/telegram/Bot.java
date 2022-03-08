package ru.liga.telegram;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.liga.helpers.RequestHelper;
import ru.liga.model.Currency;
import ru.liga.model.Request;

import java.util.Optional;

@Slf4j
public final class Bot extends TelegramLongPollingBot {
    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }

    }
    @SneakyThrows
    private void handleMessage(Message message) {
        // We check if the update has a message and the message has text
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> entity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (entity.isPresent()) {
                String command = message.getText();
                log.info(command);
                Request request = new Request(command);
                RequestHelper requestHelper = new RequestHelper();
                execute(SendMessage.builder().text(requestHelper.executeRequest(request))
                        .chatId(message.getChatId().toString())
                        .build());
            }

        }
    }

    @Override
    public String getBotUsername() {
        return "TradingAssistantKofBot";
    }

    @Override
    public String getBotToken() {
        return "5232908637:AAGvwWcK5-qb98gmdQiXR58Xtpk3zbJ41aE";
    }
}
