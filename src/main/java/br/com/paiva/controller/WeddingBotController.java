package br.com.paiva.controller;

import br.com.paiva.memory.ChatMemoryBean;
import br.com.paiva.model.Gift;
import br.com.paiva.service.GiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bot")
public class WeddingBotController {

    private final ChatMemoryBean chatMemoryBean;

    private final GiftService giftService;

    @Autowired
    public WeddingBotController(ChatMemoryBean chatMemoryBean, GiftService giftService) {
        this.chatMemoryBean = chatMemoryBean;
        this.giftService = giftService;
    }

    @GetMapping("/status")
    public String checkStatus() {
        return "Bot is running!";
    }

    @DeleteMapping("/clear-memory")
    public String clearMemory() {
        return chatMemoryBean.clearMemory();
    }

    @DeleteMapping("/clear-memory/{id}")
    public String clearMemory(@PathVariable String id) {
        return chatMemoryBean.clearMemoryById(id);
    }

    @PostMapping("/register/gift")
    public String registerGift(@RequestBody Gift body){
        giftService.createGift(body);
        return "Gift register succesfully";
    }

}
