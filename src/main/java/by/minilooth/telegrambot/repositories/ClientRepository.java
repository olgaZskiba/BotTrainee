package by.minilooth.telegrambot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import by.minilooth.telegrambot.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    
}
