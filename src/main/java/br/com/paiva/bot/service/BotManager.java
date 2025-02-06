package br.com.paiva.bot.service;

import br.com.paiva.bot.handler.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class BotManager extends AbilityBot {

    private final ResponseHandler responseHandler;

    @Autowired
    public BotManager(Environment env, ResponseHandler responseHandler) {
        super(env.getProperty("telegram.bot-token"), env.getProperty("telegram.bot-username"));
        this.responseHandler = responseHandler;
    }

    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String receivedMessage = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            executeSendMessage(chatId, responseHandler.replyToMessages(chatId, receivedMessage));
        }
    }

    private void executeSendMessage(long chatId, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        silent.execute(sendMessage);
    }

    @Override
    public long creatorId() {
        return 1L;
    }

}
