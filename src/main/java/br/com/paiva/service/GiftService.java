package br.com.paiva.service;

import br.com.paiva.exception.UserNotFoundException;
import br.com.paiva.model.Gift;
import br.com.paiva.model.User;
import br.com.paiva.repository.GiftRepository;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiftService {

    @Autowired
    private GiftRepository giftRepository;

    @Autowired
    private UserService userService;

    @Tool("lista os presentes que o casal já recebeu")
    public String getAvailableGifts() {
        List<Gift> gifts = giftRepository.findByStatus("reserved");

        if (gifts.isEmpty()) {
            return "Nenhum presente foi recebido até o momento.";
        }

        StringBuilder response = new StringBuilder("Presentes já reservados:\n");
        for (Gift gift : gifts) {
            response.append(gift.getName()).append(")\n");
        }
        return response.toString();
    }

    @Tool("registra o presente que o usuário vai presentear")
    public void registerGiftByUser(@ToolMemoryId Long chatId, String giftName) throws UserNotFoundException {
        String name = userService.getUser(chatId)
                .map(User::getName)
                .orElseThrow(() -> new UserNotFoundException(String.format("Usuário não encontrado para o chatId %s!", chatId)));
        Gift gift = new Gift(chatId, giftName, "reserved", name);
        giftRepository.save(gift);
    }

    public void createGift(Gift gift) {
        giftRepository.save(gift);
    }

}
