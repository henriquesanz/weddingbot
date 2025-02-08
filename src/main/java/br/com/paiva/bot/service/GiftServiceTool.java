package br.com.paiva.bot.service;

import br.com.paiva.bot.exception.UserNotFoundException;
import br.com.paiva.bot.model.Gift;
import br.com.paiva.bot.model.User;
import br.com.paiva.bot.repository.GiftRepository;
import br.com.paiva.bot.repository.UserRepository;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiftServiceTool {

    private final GiftRepository giftRepository;

    private final UserService userService;

    public GiftServiceTool(GiftRepository giftRepository, UserService userService) {
        this.giftRepository = giftRepository;
        this.userService = userService;
    }

    @Tool("lista os presentes que o casal já recebeu")
    public String getAvailableGifts() {
        List<Gift> gifts = giftRepository.findByStatus("Reserved");

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

    public void createGift(String giftName, String status, String reservedBy) {
        Gift gift = new Gift(giftName, status, reservedBy);
        giftRepository.save(gift);
    }

}
