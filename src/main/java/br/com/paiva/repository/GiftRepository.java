package br.com.paiva.repository;

import br.com.paiva.model.Gift;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiftRepository extends MongoRepository<Gift, String> {
    List<Gift> findByStatus(String status);
    Gift findByName(String name);
}
