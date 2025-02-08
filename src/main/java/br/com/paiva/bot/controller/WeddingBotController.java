package br.com.paiva.bot.controller;

import br.com.paiva.bot.memory.ChatMemoryBean;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bot")
public class WeddingBotController {

    private final ChatMemoryBean chatMemoryBean;

    public WeddingBotController(ChatMemoryBean chatMemoryBean) {
        this.chatMemoryBean = chatMemoryBean;
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

}
