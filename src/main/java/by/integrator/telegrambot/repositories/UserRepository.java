package by.integrator.telegrambot.repositories;

import by.integrator.telegrambot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByTelegramId(String telegramId);
}
