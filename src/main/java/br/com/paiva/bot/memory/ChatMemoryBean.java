package br.com.paiva.bot.memory;

import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatMemoryBean implements ChatMemoryProvider {

    private final Map<Object, ChatMemory> memories = new ConcurrentHashMap<>();

    @Override
    public ChatMemory get(Object memoryId) {
        return memories.computeIfAbsent(memoryId, id -> MessageWindowChatMemory.builder()
                .maxMessages(20)
                .id(memoryId)
                .build());
    }

    @PreDestroy
    public void close() {
        memories.clear();
    }

}
