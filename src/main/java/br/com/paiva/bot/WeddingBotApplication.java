package br.com.paiva.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class WeddingBotApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext ctx = SpringApplication.run(WeddingBotApplication.class, args);
		//This configuration is necessary because is 3.x Spring Version the Telegram Lib not initialize automatically de Bean
		try {
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(ctx.getBean("botManager", AbilityBot.class));
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

}
