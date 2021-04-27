package by.integrator.telegrambot.service;

import by.integrator.telegrambot.model.Client;
import by.integrator.telegrambot.model.Messenger;
import by.integrator.telegrambot.repositories.MessengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
public class MessengerService {

    @Autowired
    private ClientService clientService;
    @Autowired
    private MessengerRepository messengerRepository;

    @Transactional
    public void save(Messenger messenger) {
        messengerRepository.save(messenger);
    }

    @Transactional
    public void delete(Messenger messenger) {
        messengerRepository.delete(messenger);
    }

    @Transactional
    public List<Messenger> getAll() {
        return messengerRepository.findAll();
    }

    @Transactional
    public Messenger getById(Integer id) {
        return messengerRepository.findById(id.longValue()).orElse(null);
    }

    public void addMessengerToClient(Client client, Messenger messenger) {
        List<Messenger> messengers = getAllByClient(client);

        messengers.add(messenger);

        client.setMessengers(new HashSet<>(messengers));

        clientService.save(client);
    }

    public void removeMessengerFromClient(Client client, Messenger messenger) {
        List<Messenger> messengers = getAllByClient(client);

        messengers.remove(messenger);

        client.setMessengers(new HashSet<>(messengers));

        clientService.save(client);
    }

    public List<Messenger> getAllByClient(Client client) {
        return messengerRepository.findAllByClients(client);
    }

    public String getClientMessengerList(Client client) {
        List<Messenger> messengerList = getAllByClient(client);
        String list = "";
        if (!messengerList.isEmpty()) {
            for (Messenger messenger : messengerList) {
                list = list.concat(messenger.getName());
                list = list.concat(", ");
            }
            return list.substring(0, list.length() - 2);
        }
        return "не заполнено";
    }
}
