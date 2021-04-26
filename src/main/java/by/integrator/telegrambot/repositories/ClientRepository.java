package by.integrator.telegrambot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import by.integrator.telegrambot.model.Client;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findAllByProfileFilledFalse();
    List<Client> findAllByProfileFilledTrue();

    List<Client> findAllByProcessedTrue();
    List<Client> findAllByProcessedFalse();
}
