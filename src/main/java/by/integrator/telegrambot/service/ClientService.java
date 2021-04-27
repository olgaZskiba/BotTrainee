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

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private MessengerService messengerService;
    @Autowired
    private UserService userService;

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
                .firstName(user.getFirstname())
                .user(user)
                .profileFilled(false)
                .processed(false)
                .fillStarted(false)
                .problem("")
                .day(1)
                .build();

        user.setClient(client);
//        userService.save(user);

        return client;
    }

    public Boolean isHaveMessenger(Client client, Messenger messenger) {
        return messengerService.getAllByClient(client).contains(messenger);
    }

    @Transactional
    public List<Client> getByProfileFilledFalse() {
        return clientRepository.findAllByProfileFilledFalse();
    }

    @Transactional
    public List<Client> getByProfileFilledTrue() {
        return clientRepository.findAllByProfileFilledTrue();
    }

    @Transactional
    public List<Client> getByProcessedFalse() {
        return clientRepository.findAllByProcessedFalse();
    }

    @Transactional
    public List<Client> getByProcessedTrue() {
        return clientRepository.findAllByProcessedTrue();
    }

    @Transactional
    public List<Client> getByFillStartedFalse() {
        return clientRepository.findAllByFillStartedFalse();
    }

    @Transactional
    public List<Client> getByFillStartedTrue() {
        return clientRepository.findAllByFillStartedTrue();
    }


    public Integer countAllClients() {
        return clientRepository.findAll().size();
    }

    public Client getById(long parseInt) {
        return clientRepository.findById(parseInt).orElse(null);
    }

    public void removeCurrents(Client client) {
        client.setQuestions(null);
        save(client);
    }

    public String checkProblem(String currentText, String newText) {
        String text = "";
        if (currentText != null) {
            String[] parse = currentText.split(";");
            if (parse.length != 0) {
                for (String current : parse) {
                    System.out.println(current);
                    if (current.equals(newText.substring(0, newText.length() - 1))) {
                        return currentText.replaceAll(newText, "");
                    } else {
                        return currentText.concat(newText);
                    }
                }
            }
        }
        return text;
    }
}
