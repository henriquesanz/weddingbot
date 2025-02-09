package br.com.paiva.service;

import br.com.paiva.model.User;
import br.com.paiva.repository.UserRepository;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean isNewUser(Long chatId) {
        return userRepository.findByChatId(chatId).isEmpty();
    }

    public Optional<User> getUser(Long chatId){
        return userRepository.findByChatId(chatId);
    }

    public boolean contactHasBeenSent(Long chatId) {
        Optional<User> user = userRepository.findByChatId(chatId);
        return user.map(User::isContactSent).orElse(false);
    }

    public void createUser(Long chatId) {
        User user = new User(chatId);
        userRepository.save(user);
    }

    public void updateUserInfo(Long chatId, String name, String contact, String notes) {
        Optional<User> userOptional = userRepository.findByChatId(chatId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (name != null) user.setName(name);
            if (contact != null) user.setContact(contact);
            if (notes != null) user.setNotes(notes);
            userRepository.save(user);
        }
    }

    public void updateContactSent(Long chatId){
        Optional<User> userOptional = userRepository.findByChatId(chatId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setContactSent(true);
            userRepository.save(user);
        }
    }

    @Tool("saves a message from the user to the bride and groom")
    public void saveUserNotes(@ToolMemoryId Long chatId, String message){
        Optional<User> userOptional = userRepository.findByChatId(chatId);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setNotes(message);
            userRepository.save(user);
        }
    }
}

