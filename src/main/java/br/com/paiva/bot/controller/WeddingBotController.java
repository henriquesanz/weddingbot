package br.com.paiva.bot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bot")
public class WeddingBotController {

    @GetMapping("/status")
    public String checkStatus() {
        return "Bot is running!";
    }

}
