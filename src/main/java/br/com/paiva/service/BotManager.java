package br.com.paiva.service;

import br.com.paiva.utils.Constants;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class BotManager extends AbilityBot {

    private final ResponseHandler responseHandler;

    private final UserService userService;

    @Autowired
    public BotManager(Environment env, @Lazy ResponseHandler responseHandler,@Lazy UserService userService) {
        super(env.getProperty("telegram.bot-token"), env.getProperty("telegram.bot-username"));
        this.responseHandler = responseHandler;
        this.userService = userService;
    }

    public void onUpdateReceived(Update update) {
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

                firstContact(chatId);
            } else {
                if(userService.contactHasBeenSent(chatId)) {
                    silent.execute(new SendChatAction(chatId.toString(), "typing", update.getUpdateId()));
                    executeSendMessage(chatId, responseHandler.replyToMessages(chatId, receivedMessage));
                }else {
                    executeSendMessage(chatId, Constants.REQUEST_CONTACT);
                }

            }
        }
    }

    private void firstContact(Long chatId) {
        KeyboardButton contactButton = new KeyboardButton("Send contact");
        contactButton.setRequestContact(true);

        KeyboardRow row = new KeyboardRow();
        row.add(contactButton);

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row);

        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup();
        markup.setKeyboard(keyboard);
        markup.setResizeKeyboard(true);
        markup.setOneTimeKeyboard(true);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(Constants.FIRST_CONTACT);
        sendMessage.setReplyMarkup(markup);
        sendMessage.enableMarkdown(true);

        silent.execute(sendMessage);
    }

    private void executeSendMessage(long chatId, String text){

        ReplyKeyboardRemove removeKeyboard = new ReplyKeyboardRemove();
        removeKeyboard.setRemoveKeyboard(true);

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(removeKeyboard);

        silent.execute(sendMessage);
    }

    @Tool("send the pix to the user")
    @Lazy
    public void sendRequestedPix(@ToolMemoryId Long chatId){
        executeSendMessage(chatId, Constants.FORMATTED_PIX_SENT);
        executeSendMessage(chatId, Constants.PIX_NUMBER);
    }

    @Override
    public long creatorId() {
        return 1L;
    }

}
