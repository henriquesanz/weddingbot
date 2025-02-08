package br.com.paiva.bot.service;

import br.com.paiva.bot.exception.UserNotFoundException;
import br.com.paiva.bot.model.User;
import br.com.paiva.bot.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResponseHandler {

    @Autowired
    @Lazy
    private Assistant assistant;

    private final UserService userService;

    public ResponseHandler(UserService userService) {
        this.userService = userService;
    }

    public String replyToMessages(long chatId, String message) {

        try{
            log.info("Request for ChatId ({}): {}", chatId, message);

            String response;

            if (userService.isNewUser(chatId)) {
                userService.createUser(chatId);
                response = Constants.START_TEXT;
            } else {
                String name = userService.getUser(chatId)
                        .map(User::getName)
                        .orElseThrow(() -> new UserNotFoundException(String.format("Usuário não encontrado para o chatId %s!", chatId)));
                response = assistant.chat(chatId, String.format("User Name: %s, Message: %s", name, message)).replaceAll("(?s)<think>.*?</think>", "").trim();
            }

            log.info("Response for ChatId ({}): {}", chatId, response);

            return response;
        }catch (UserNotFoundException e){
            log.error(e.getMessage());
            return assistant.chat(chatId, message).replaceAll("(?s)<think>.*?</think>", "").trim();
        }catch (Exception e){
            log.error("Error on get Assistant Message: {}", e.getMessage());
            return Constants.FAIL_RESPONSE;
        }

    }

}