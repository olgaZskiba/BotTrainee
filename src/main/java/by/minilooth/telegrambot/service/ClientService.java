package by.minilooth.telegrambot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.minilooth.telegrambot.bot.state.ClientBotState;
import by.minilooth.telegrambot.model.Client;
import by.minilooth.telegrambot.model.User;
import by.minilooth.telegrambot.repositories.ClientRepository;

@Service
public class ClientService {
    
    @Autowired private ClientRepository clientRepository;

    @Transactional
    public void save(Client client) {
        clientRepository.save(client);
    }

    @Transactional
    public void delete(Client client) {
        clientRepository.delete(client);
    }

    @Transactional
    public List<Client> getAll() {
        return clientRepository.findAll();
    }

    public Client createClient(User user) {
        Client client = Client.builder()
                              .clientBotState(ClientBotState.getInitialState())
                              .user(user)
                              .build();

        user.setClient(client);

        return client;
    }

}
