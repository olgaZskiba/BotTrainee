package by.integrator.telegrambot.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import by.integrator.telegrambot.model.User;
import by.integrator.telegrambot.model.enums.Role;
import by.integrator.telegrambot.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    @Transactional
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Transactional
    public User getByTelegramId(String telegramId) {
        return userRepository.findByTelegramId(telegramId);
    }

    @Transactional
    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User createUser(Update update, Role role) {
        User user = User.builder()
                .telegramId(update.getMessage().getFrom().getId().toString())
                .firstname(update.getMessage().getFrom().getFirstName())
                .lastname(update.getMessage().getFrom().getLastName())
                .username(update.getMessage().getFrom().getUserName())
                .botLastMessageDate(0)
                .botLastMessageId(0)
                .botLastMessageEditable(false)
                .role(role)
                .build();

        this.save(user);

        return user;
    }

}
