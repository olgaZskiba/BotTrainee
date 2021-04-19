package by.integrator.telegrambot.repositories;

import by.integrator.telegrambot.model.Client;
import by.integrator.telegrambot.model.Messenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessengerRepository extends JpaRepository<Messenger, Long> {
    List<Messenger> findAllByClients(Client client);
}
