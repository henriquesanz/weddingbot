package br.com.paiva.bot.handler;

import br.com.paiva.bot.service.Assistant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ResponseHandler {

    @Autowired
    private Assistant assistant;

    public String replyToMessages(long chatId, String message) {

        try{
            System.out.println("Request: " + message);

            String response = assistant.chat(chatId, message).replaceAll("(?s)<think>.*?</think>", "").trim();

            System.out.println("Response: " + response);

            return response;
        }catch (Exception e){
            System.out.println("ERROR: " + e.getMessage());
            return "Não consegui processar sua pergunta, por favor, tente novamente em instantes.";
        }

    }

}