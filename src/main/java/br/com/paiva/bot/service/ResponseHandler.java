package br.com.paiva.bot.service;

import br.com.paiva.bot.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResponseHandler {

    @Autowired
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
                response = assistant.chat(chatId, message).replaceAll("(?s)<think>.*?</think>", "").trim();
            }

            log.info("Response for ChatId ({}): {}", chatId, response);

            return response;
        }catch (Exception e){
            System.out.println("ERROR: " + e.getMessage());
            return Constants.FAIL_RESPONSE;
        }

    }

}