package br.com.paiva.bot.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface Assistant {

    @SystemMessage(fromResource = "prompts/assistant.txt")
    String chat(@MemoryId Long id, @UserMessage String msg);
}
