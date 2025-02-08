package br.com.paiva.bot.repository;

import br.com.paiva.bot.model.Gift;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GiftRepository extends MongoRepository<Gift, String> {
    List<Gift> findByStatus(String status);
    Gift findByName(String name);
}
