package ru.liga.version2.telegram;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import ru.liga.version2.exception.RequestException;
import ru.liga.version2.helper.RequestHelper;
import ru.liga.version2.model.Request;

import java.time.format.DateTimeParseException;
import java.util.Optional;

@Slf4j
public final class Bot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            try {
                handleMessage(update.getMessage());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMessage(Message message) throws TelegramApiException {
        // We check if the update has a message and the message has text
        if (message.hasText() && message.hasEntities()) {
            Optional<MessageEntity> entity =
                    message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (entity.isEmpty()) {
                return;
            }
            String command = message.getText();
            log.info(command);
            executeCommand(message, command);
        }
    }

    private void executeCommand(Message message, String command) throws TelegramApiException {

        try {
            Request request = new Request(command);
            RequestHelper requestHelper = new RequestHelper(request);
            execute(SendMessage.builder().text(command + " выполняется")
                    .chatId(message.getChatId().toString())
                    .build());
            if (!request.getOutput().equals("graph")) {
                execute(SendMessage.builder().text(requestHelper.executeTextRequest())
                        .chatId(message.getChatId().toString())
                        .build());
            } else {
                execute(SendPhoto.builder().photo(requestHelper.executeGraphRequest())
                        .chatId(message.getChatId().toString())
                        .build());
            }
        } catch (DateTimeParseException e) {
            execute(SendMessage.builder().text("Неправильно введена дата")
                    .chatId(message.getChatId().toString())
                    .build());
        } catch (TelegramApiRequestException | RequestException e) {
            execute(SendMessage.builder().text(e.getMessage())
                    .chatId(message.getChatId().toString())
                    .build());
        } catch (Exception e) {
            execute(SendMessage.builder().text("Ошибка")
                    .chatId(message.getChatId().toString())
                    .build());
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
