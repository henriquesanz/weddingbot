package br.com.paiva.bot.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "users")
@Data
public class User {

    @Id
    private Long chatId;
    private Instant firstMessage;
    private String name;
    private String contact;
    private boolean contactSent;
    private String notes;

    public User(Long chatId){
        this.chatId = chatId;
    }

}
