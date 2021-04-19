package by.integrator.telegrambot.service;

import java.util.List;

import by.integrator.telegrambot.model.Messenger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.integrator.telegrambot.bot.api.client.states.ClientBotState;
import by.integrator.telegrambot.model.Client;
import by.integrator.telegrambot.model.User;
import by.integrator.telegrambot.repositories.ClientRepository;

@Service
public class ClientService {
    
    @Autowired private ClientRepository clientRepository;
    @Autowired private MessengerService messengerService;

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

    public Boolean isHaveMessenger(Client client, Messenger messenger) {
        return messengerService.getAllByClient(client).contains(messenger);
    }

}
