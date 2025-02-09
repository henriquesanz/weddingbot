package br.com.paiva.service;

import br.com.paiva.utils.Constants;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class BotManager implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    private final ResponseHandler responseHandler;

    private final UserService userService;

    private final Environment env;

    @Autowired
    public BotManager(ResponseHandler responseHandler, UserService userService, Environment environment) {
        this.responseHandler = responseHandler;
        this.userService = userService;
        this.env = environment;
        telegramClient = new OkHttpTelegramClient(getBotToken());
    }

    @Override
    public String getBotToken() {
        return env.getProperty("telegram.bot-token");
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        if(update.getMessage().hasContact()){
            Long chatId = update.getMessage().getChatId();
            String fullName = String.format("%s %s", update.getMessage().getContact().getFirstName(), update.getMessage().getContact().getLastName());
            String phoneNumber = update.getMessage().getContact().getPhoneNumber();
            userService.updateUserInfo(chatId, fullName, phoneNumber, null);

            executeSendMessage(chatId, Constants.START_TEXT);
            userService.updateContactSent(chatId);
        }
        if (update.hasMessage() && (update.getMessage().hasText())) {
            String receivedMessage = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            if (userService.isNewUser(chatId)) {
                userService.createUser(chatId);

                try {
                    firstContact(chatId);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else {
                if(userService.contactHasBeenSent(chatId)) {
                    try {
                        telegramClient.execute(new SendChatAction(chatId.toString(), "typing"));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    executeSendMessage(chatId, responseHandler.replyToMessages(chatId, receivedMessage));
                }else {
                    executeSendMessage(chatId, Constants.REQUEST_CONTACT);
                }

            }
        }
    }

    private void firstContact(Long chatId) throws TelegramApiException {
        KeyboardButton contactButton = new KeyboardButton("Send contact");
        contactButton.setRequestContact(true);

        KeyboardRow row = new KeyboardRow();
        row.add(contactButton);

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row);

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(keyboard);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);

        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), Constants.FIRST_CONTACT);
        sendMessage.setReplyMarkup(markup);
        sendMessage.enableMarkdown(true);

        telegramClient.execute(sendMessage);
    }

    private void executeSendMessage(long chatId, String text){

        ReplyKeyboardRemove removeKeyboard = new ReplyKeyboardRemove(true);

        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), text);
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(removeKeyboard);

        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Tool("send the pix to the user")
    @Lazy
    public void sendRequestedPix(@ToolMemoryId Long chatId){
        executeSendMessage(chatId, Constants.FORMATTED_PIX_SENT);
        executeSendMessage(chatId, Constants.PIX_NUMBER);
    }

}
