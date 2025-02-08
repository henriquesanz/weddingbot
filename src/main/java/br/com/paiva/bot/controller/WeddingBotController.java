package br.com.paiva.bot.controller;

import br.com.paiva.bot.memory.ChatMemoryBean;
import br.com.paiva.bot.model.Gift;
import br.com.paiva.bot.service.GiftServiceTool;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bot")
public class WeddingBotController {

    private final ChatMemoryBean chatMemoryBean;

    private final GiftServiceTool giftServiceTool;

    public WeddingBotController(ChatMemoryBean chatMemoryBean, GiftServiceTool giftServiceTool) {
        this.chatMemoryBean = chatMemoryBean;
        this.giftServiceTool = giftServiceTool;
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
        giftServiceTool.createGift(body.getName(), body.getStatus(), body.getReservedBy());
        return "Gift register succesfully";
    }

}
