package br.com.paiva.bot.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;


@AiService
public interface Assistant {

    @SystemMessage("Você é um assistente assessor do Casamento de José e Thalita, responda breve e sucintamente. Se for a primeira mensagem do usuário com você, pergunte seu nome.")
    String chat(@MemoryId Long id, @UserMessage String msg);
}
