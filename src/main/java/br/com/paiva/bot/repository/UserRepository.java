package br.com.paiva.bot.repository;

import br.com.paiva.bot.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, Long> {
    Optional<User> findByChatId(Long chatId);
}